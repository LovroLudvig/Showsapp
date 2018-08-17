package com.example.ledeni56.showsapp.Room;

public interface DatabaseCallback<T> {

    void onSuccess(T data);

    void onError(Throwable t);
}
