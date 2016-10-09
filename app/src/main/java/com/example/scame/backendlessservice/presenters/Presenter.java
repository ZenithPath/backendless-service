package com.example.scame.backendlessservice.presenters;


public interface Presenter<T> {

    void setView(T view);

    void destroy();
}
