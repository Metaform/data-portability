package org.dataportabilityproject.transfer.microsoft.transformer;

import ezvcard.VCard;
import ezvcard.property.Address;
import org.dataportabilityproject.transfer.microsoft.transformer.contacts.PhysicalAddressTransformer;
import org.dataportabilityproject.transfer.microsoft.transformer.contacts.VCardTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * Currently this implementation assumes a 1:1 relationship between the result and input types since a result type is mapped to a single transformer function.
 * This could be extended if needed to map N:N by keying off result and input types.
 */
public class TransformerServiceImpl implements TransformerService {
    Map<Class<?>, BiFunction<?, ?, ?>> cache = new HashMap<>();

    public TransformerServiceImpl() {
        initContactTransformers();
    }

    @Override
    public <T> TransformResult<T> transform(Class<T> resultType, Object input) {
        TransformerContext context = new TransformerContextImpl();
        T dataType = transform(resultType, input, context);
        return new TransformResult<>(dataType, context.getProblems());
    }

    @SuppressWarnings("unchecked")
    private <T> T transform(Class<T> resultType, Object input, TransformerContext context) {
        BiFunction<Object, TransformerContext, T> function = (BiFunction<Object, TransformerContext, T>) cache.computeIfAbsent(resultType, v -> {
            throw new IllegalArgumentException("Unsupported transform type: " + resultType);
        });
        return function.apply(input, context);
    }

    private class TransformerContextImpl implements TransformerContext {
        private List<String> problems = new ArrayList<>();

        @Override
        public <T> T transform(Class<T> resultType, Object input, TransformerContext context) {
            return TransformerServiceImpl.this.transform(resultType, input, context);
        }

        @Override
        public void problem(String message) {
            problems.add(message);
        }

        @Override
        public List<String> getProblems() {
            return problems;
        }
    }

    private void initContactTransformers() {
        cache.put(VCard.class, new VCardTransformer());
        cache.put(Address.class, new PhysicalAddressTransformer());
    }


}
