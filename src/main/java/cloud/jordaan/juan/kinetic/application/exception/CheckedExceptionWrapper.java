package cloud.jordaan.juan.kinetic.application.exception;

import java.util.function.BiFunction;
import java.util.function.Function;

public interface CheckedExceptionWrapper {

	public static <R, T> R thowsRuntimeException(Function<T, R> f, T t) {
		try {
			return f.apply(t);
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static <R, T, U> R thowsRuntimeException(BiFunction<T, U, R> f, T t, U u) {
		try {
			return f.apply(t, u);
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
}
