package me.yic.xconomy.utils;

public enum UUIDMode {
    DEFAULT("UUID-Mode: Default"),
    ONLINE("UUID-Mode: Online"),
    OFFLINE("UUID-Mode: Offline"),
    SEMIONLINE("UUID-Mode: SemiOnline");

    final String value;

    UUIDMode(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }


    public boolean equals(UUIDMode Other) {
        return this.value.equals(Other.value);
    }
}
