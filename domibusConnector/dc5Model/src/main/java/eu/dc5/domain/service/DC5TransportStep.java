package eu.dc5.domain.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DC5TransportStep extends DC5ProcessStep{
    private Long id;
    private String message; // TODO: message?
}
