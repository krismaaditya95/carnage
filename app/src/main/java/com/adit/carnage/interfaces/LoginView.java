package com.adit.carnage.interfaces;

public interface LoginView {

    void updateTextState(String info);
    void showBothError();
    void showErrorUsername(Integer code);
    void showErrorPassword(Integer code);
    void valid();
}
