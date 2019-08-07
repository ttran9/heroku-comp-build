package tran.compbuildbackend.domain.computerbuild;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "OverclockingNote")
public class OverclockingNote extends AbstractNote {

    public OverclockingNote() {
        super();
    }
}
