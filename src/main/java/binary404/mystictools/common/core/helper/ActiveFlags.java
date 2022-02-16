package binary404.mystictools.common.core.helper;

public enum ActiveFlags {
    IS_SILKY_MINING,
    IS_LUCKY_MINING,
    IS_DIRECT_MINING;

    ActiveFlags() {
        this.activeReferences = 0;
    }

    public boolean isSet() {
        return (this.activeReferences > 0);
    }
    private int activeReferences;

    public synchronized void runIfNotSet(Runnable run) {
        if(!isSet()) {
            this.activeReferences++;
            try {
                run.run();
            }finally {
                this.activeReferences--;
            }
        }
    }
}
