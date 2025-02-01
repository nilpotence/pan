package fr.nilpo.pan.models.boulders;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoulderIndexData {
    private UUID id;
    private String name;
    private String estimatedGrade;
    private int obsoleteCount;
    private long score;

    private boolean ticked;
}
