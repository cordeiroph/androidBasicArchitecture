package com.phrc.androidarchitecture.repository;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.MaybeEmitter;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DataRequestManager<DBRequest, ApiResponse> {

    public interface DbQuery<T>{
        Flowable<T> executeDbQuery();
    }

    public interface ApiRequest<R>{
        Single<Response<R>> executeApiRequest();
    }

    public interface ParseResult<T, R>{
        T parseApiResultOnDbResult(R response);
    }

    public interface SaveOnDb<R>{
        void saveApiResultOnDataBase(R apiResult) throws Exception;
    }

    private Flowable<DataRequestModel<DBRequest>> data;
    private DbQuery<DBRequest> dbQuery;
    private ApiRequest<ApiResponse> apiRequest;
    private ParseResult<DBRequest, ApiResponse> parseResult;
    private SaveOnDb<ApiResponse> saveOnDb;

    @Inject
    Retrofit retrofit;

    public DataRequestManager() {
//        PawTrailsApplication.applicationContext.getAppComponent().inject(this);
    }

    /**
     * @param dbQuery database query
     * @param apiRequest Api Request
     * @param saveOnDb Method to save api result on db
     * @param parseResult Method to parse the Api result into a expected result
     * @param returnApiResult if true, it will send api result in case of success, default true in
     *                        case is only being apply api activityMonitorRequester and default false when is calling db and api
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T, R>DataRequestManager<T, R> build(DbQuery<T> dbQuery, ApiRequest<R> apiRequest,
                                                       SaveOnDb<R> saveOnDb, ParseResult<T, R> parseResult,
                                                       final boolean returnApiResult) {
        DataRequestManager<T, R> dataRequestManager = new DataRequestManager<>();
        dataRequestManager.dbQuery = dbQuery;
        dataRequestManager.apiRequest = apiRequest;
        dataRequestManager.parseResult = parseResult;
        dataRequestManager.saveOnDb = saveOnDb;
        dataRequestManager.buildData(returnApiResult);
        return dataRequestManager;
    }

    /**
     *
     * @param dbQuery database query
     * @param apiRequest Api Request
     * @param saveOnDb Method to save api result on db
     * @param <T>
     * @param <R>
     * @return observes the DB, executes an API activityMonitorRequester, saves the result in DB and after parsing it
     * returns in the UI if there is an API error
     */
    public static <T, R>DataRequestManager<T, R> build(DbQuery<T> dbQuery, ApiRequest<R> apiRequest, SaveOnDb<R> saveOnDb){
        return build(dbQuery, apiRequest, saveOnDb, response -> (T) null, false);
    }

//    /**
//     *
//     * @param dbQuery database query
//     * @param apiRequest Api Request
//     * @param saveOnDb Method to save api result on db
//     * @param <E>
//     * @return observes the DB, executes an API activityMonitorRequester which saves the result in DB and returns the API error if any in UI
//     */
//    public static <E>DataRequestManager<E, E> build(DbQuery<E> dbQuery, ApiRequest<E> apiRequest,
//                                                    SaveOnDb<E> saveOnDb){
//        return build(dbQuery, apiRequest, saveOnDb, response -> response);
//    }

    /**
     *
     * @param apiRequest Api Request
     * @param parseResult Method to parse the Api result into a expected result
     * @param <T>
     * @param <R>
     * @return an API activityMonitorRequester and after parsing it returns the result in UI
     */
    public static <T, R>DataRequestManager<T, R> build(ApiRequest<R> apiRequest, ParseResult<T, R> parseResult){
        return build(Flowable::empty, apiRequest, apiResult -> {}, parseResult, true);
    }

    /**
     *
     * @param apiRequest Api Request
     * @param <E>
     * @return a simple API activityMonitorRequester
     */
    public static <E>DataRequestManager<E, E> build(ApiRequest<E> apiRequest){
        return build(apiRequest, response -> response);
    }

    /**
     *
     * @param apiRequest Api Request
     * @param saveOnDb Method to save api result on db
     * @param <E>
     * @return an API activityMonitorRequester which saves the result in DB
     */
    public static <E>DataRequestManager<E, E> build(ApiRequest<E> apiRequest, SaveOnDb<E> saveOnDb){
        return build(apiRequest, saveOnDb, response -> response);
    }

    /**
     * @param apiRequest Api Request
     * @param saveOnDb Method to save api result on db
     * @param parseResult Method to parse the Api result into a expected result
     * @param <T>
     * @param <R>
     * @return an API activityMonitorRequester which saves the result in DB and after parsing it returns the result in UI
     */
    public static <T, R>DataRequestManager<T, R> build(ApiRequest<R> apiRequest, SaveOnDb<R> saveOnDb,
                                                       ParseResult<T, R> parseResult){
        return build(Flowable::empty, apiRequest, saveOnDb, parseResult, true);
    }

    @WorkerThread
    private void buildData(boolean returnApiResult) {
        data =
                Flowable.
                        merge(
                                dbQuery
                                        .executeDbQuery()
                                        .subscribeOn(Schedulers.io())
                                        .map(DataRequestModel.Companion::onSuccess)
                                        .onErrorReturn(DataRequestModel.Companion::onError)
                                ,
                                updateData(returnApiResult)
                                        .subscribeOn(Schedulers.io())
                                        .map(requestUIModel -> requestUIModel.convert(
                                                (requestUIModel.getContent() == null) ? null :
                                                        parseResult.parseApiResultOnDbResult(requestUIModel.getContent()))
                                        )
                                        .onErrorReturn(DataRequestModel.Companion::onError)
                                        .toFlowable()
                                        .delay((returnApiResult)?0:400, TimeUnit.MILLISECONDS)
                        )

                        .startWith(DataRequestModel.Companion.inProgress())
                        .onErrorReturn(DataRequestModel.Companion::onError)
                        .distinctUntilChanged();
    }

    private Maybe<DataRequestModel<ApiResponse>> updateData(boolean returnApiResult) {
        return apiRequest
                .executeApiRequest()
                .flatMapMaybe(dataRequest -> {
                    if (dataRequest.isSuccessful()){
                        return Maybe.create((MaybeEmitter<DataRequestModel<ApiResponse>> e) -> {
                            try {
                                saveOnDb.saveApiResultOnDataBase(dataRequest.body());
                                if (returnApiResult) {
                                    e.onSuccess(DataRequestModel.Companion.onSuccess(dataRequest));
                                } else {
                                    e.onComplete();
                                }
                            } catch (Exception error) {
                                error.printStackTrace();
                                e.onSuccess(DataRequestModel.Companion.onError(dataRequest));
                            }
                        }).subscribeOn(Schedulers.io());
                    } else {
                        return Maybe.just(DataRequestModel.Companion.onError(dataRequest));
                    }
                });
    }

    public Flowable<DataRequestModel<DBRequest>> asFlowable(){
        return data;
    }

    public LiveData<DataRequestModel<DBRequest>> asLiveData(){
        return LiveDataReactiveStreams.fromPublisher(data);
    }
}