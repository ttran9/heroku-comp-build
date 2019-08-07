package tran.compbuildbackend.domain.computerbuild;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "Purpose")
public class Purpose extends AbstractNote {

    public Purpose() {
        super();
    }
}
