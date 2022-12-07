package eu.ecodex.dc5.message.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Builder(toBuilder = true)
public class Digest {


    @NonNull
    private final String digestValue; //as hex
    @NonNull
    private final String digestAlgorithm;
}
