package ru.doxhost.newhost.server.lib;

/**
 * In which mode app is running. Available prod, test, dev.
 */
public enum Nh2Mode {

    PROD(Nh2ModeConst.MODE_PROD),

    DEV(Nh2ModeConst.MODE_DEV),

    TEST(Nh2ModeConst.MODE_TEST);

    private String mode;

    Nh2Mode(String mode) {
        this.mode = mode;
    }

    public String toString() {
        return mode;
    }
}
