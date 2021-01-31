package com.shengfei.utils;



import java.util.function.Consumer;

public class ObjectUtil {

    public ObjectUtil() {
    }

    public static <TObject> InitObject<TObject> initObject(TObject obj) {
        return new InitObject(obj);
    }

    public static class InitObject<TObject> {
        private TObject ref;

        public InitObject(TObject obj) {
            this.ref = obj;
        }

        public InitObject<TObject> init(Consumer<TObject> init) {
            if (init != null) {
                init.accept(this.ref);
            }
            return this;
        }
        public TObject getObject() {
            return this.ref;
        }
    }
}
