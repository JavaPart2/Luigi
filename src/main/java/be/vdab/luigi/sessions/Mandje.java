package be.vdab.luigi.sessions;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import javax.validation.constraints.Email;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

@Component
@SessionScope
public class Mandje implements Serializable {
    private static final int serialVersionUID = 1;
    private final Set<Integer> ids = new LinkedHashSet<>();

    public Mandje() {
    }

    public Set<Integer> getIds() {
        return ids;
    }

    public void voegToe(int id){
        ids.add(id);
    }
}
