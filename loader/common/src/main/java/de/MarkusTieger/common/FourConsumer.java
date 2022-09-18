package de.MarkusTieger.common;

import java.util.Objects;

@FunctionalInterface
public interface FourConsumer<T, E, F, G> {

	void accept(T t, E e, F f, G g);

    /**
     * Returns a composed {@code Consumer} that performs, in sequence, this
     * operation followed by the {@code after} operation. If performing either
     * operation throws an exception, it is relayed to the caller of the
     * composed operation.  If performing this operation throws an exception,
     * the {@code after} operation will not be performed.
     *
     * @param after the operation to perform after this operation
     * @return a composed {@code Consumer} that performs in sequence this
     * operation followed by the {@code after} operation
     * @throws NullPointerException if {@code after} is null
     */
    default FourConsumer<T, E, F, G> andThen(FourConsumer<? super T, ? super E, ? super F, ? super G> after) {
        Objects.requireNonNull(after);
        return (T t, E e, F f, G g) -> { accept(t, e, f, g); after.accept(t, e, f, g); };
    }

}
