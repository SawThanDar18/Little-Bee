package com.busybees.lauk_kaing_expert_services.data.vos.MyHistory;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class QuestionsListVO implements Serializable {

    @SerializedName("question_eng")
    @Expose
    private QuestionsNameVO questionsEn;

    @SerializedName("question_mm")
    @Expose
    private QuestionsNameVO questionsMm;

    @SerializedName("question_ch")
    @Expose
    private QuestionsNameVO question_Ch;

    public QuestionsNameVO getQuestionsEn() {
        return questionsEn;
    }

    public void setQuestionsEn(QuestionsNameVO questionsEn) {
        this.questionsEn = questionsEn;
    }

    public QuestionsNameVO getQuestionsMm() {
        return questionsMm;
    }

    public void setQuestionsMm(QuestionsNameVO questionsMm) {
        this.questionsMm = questionsMm;
    }

    public QuestionsNameVO getQuestion_Ch() {
        return question_Ch;
    }

    public void setQuestion_Ch(QuestionsNameVO question_Ch) {
        this.question_Ch = question_Ch;
    }
}
