package fr.nilpo.pan.models.boulders;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoulderGradeStatsData {
    private String grade;
    private long count;
}
