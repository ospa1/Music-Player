// musicServiceInterface.aidl
package com.example.clipserver;

// Declare any non-default types here with import statements

interface musicServiceInterface {

    void play(int song);
    void pause();
    void resume();
    void stop();
}
