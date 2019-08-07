package tran.compbuildbackend.domain.computerbuild;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "BuildNote")
public class BuildNote extends AbstractNote {

    public BuildNote() {
        super();
    }


}
