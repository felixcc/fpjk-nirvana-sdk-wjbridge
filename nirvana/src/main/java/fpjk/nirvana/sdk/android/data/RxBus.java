package fpjk.nirvana.sdk.android.data;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

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

    //    private final PublishSubject<Object> _bus = PublishSubject.create();

    // If multiple threads are going to emit events to this
    // then it must be made thread-safe like this instead
    private final Subject<Object, Object> _bus = new SerializedSubject<>(PublishSubject.create());

    public void send(Object o) {
        if (hasObservers()) {
            _bus.onNext(o);
        }
    }

    public Observable<Object> toObserverable() {
        return _bus;
    }

    private boolean hasObservers() {
        return _bus.hasObservers();
    }
}
