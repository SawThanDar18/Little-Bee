package com.busybees.little_bee.data.models.MyHistory;

import com.busybees.little_bee.data.vos.MyHistory.QuestionsVO;
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
    private List<QuestionsVO> questions;

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

    public List<QuestionsVO> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionsVO> questions) {
        this.questions = questions;
    }

    public List<MyHistoryDataModel> getData() {
        return data;
    }

    public void setData(List<MyHistoryDataModel> data) {
        this.data = data;
    }
}
