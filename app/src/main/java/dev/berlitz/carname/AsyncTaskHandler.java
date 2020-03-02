package dev.berlitz.carname;

public interface AsyncTaskHandler<Progress, Result> {

    void handleProgress(Progress... progress);

    void handlePostExecute(Result result);

    void handlePreExecute();

    void handleError(Exception ex);

}
