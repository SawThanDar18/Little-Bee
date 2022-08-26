package com.busybees.lauk_kaing_expert_services.data.models.MyHistory;

import com.busybees.lauk_kaing_expert_services.data.vos.MyHistory.QuestionsListVO;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MyHistoryModel {

    @SerializedName("error")
    @Expose
    private boolean error;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("question")
    @Expose
    private QuestionsListVO questions;

    @SerializedName("data")
    @Expose
    private List<MyHistoryDataModel> data;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public QuestionsListVO getQuestions() {
        return questions;
    }

    public void setQuestions(QuestionsListVO questions) {
        this.questions = questions;
    }

    public List<MyHistoryDataModel> getData() {
        return data;
    }

    public void setData(List<MyHistoryDataModel> data) {
        this.data = data;
    }
}
