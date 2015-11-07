package edu.uic.cs478.ServiceForMusic;


interface MusicPlayer {
    void playMusic(int trackNumber);
    void pauseMusic();
    void stopMusic();
    void resumeMusic();
    Bundle getHistory();
}