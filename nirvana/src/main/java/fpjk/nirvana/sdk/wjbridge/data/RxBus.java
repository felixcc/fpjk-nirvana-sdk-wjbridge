package fpjk.nirvana.sdk.wjbridge.data;

import com.jakewharton.rxrelay2.PublishRelay;
import com.jakewharton.rxrelay2.Relay;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;

/**
 * Created with Android Studio.
 * User: Felix
 * Date: 4/13/16
 * Time: 11:30 AM
 * QQ:74104
 * Email:lovejiuwei@gmail.com
 */
public class RxBus {

    private static volatile RxBus defaultInstance;

    private RxBus() {
    }

    // 单例RxBus
    public static RxBus get() {
        if (defaultInstance == null) {
            synchronized (RxBus.class) {
                if (defaultInstance == null) {
                    defaultInstance = new RxBus();
                }
            }
        }
        return defaultInstance;
    }

    private final Relay<Object> _bus = PublishRelay.create().toSerialized();

    public void send(Object o) {
        if (hasObservers()) {
            _bus.accept(o);
        }
    }

    public Flowable<Object> asFlowable() {
        return _bus.toFlowable(BackpressureStrategy.LATEST);
    }

    private boolean hasObservers() {
        return _bus.hasObservers();
    }
}
