package info.elexis.server.core.connector.elexis.jpa.model.annotated;

import java.time.LocalDate;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.7.0.v20160725-rNA")
@StaticMetamodel(Termin.class)
public class Termin_ { 

    public static volatile SingularAttribute<Termin, String> terminTyp;
    public static volatile SingularAttribute<Termin, String> grund;
    public static volatile SingularAttribute<Termin, String> extension;
    public static volatile SingularAttribute<Termin, String> erstelltVon;
    public static volatile SingularAttribute<Termin, Integer> palmId;
    public static volatile SingularAttribute<Termin, String> patId;
    public static volatile SingularAttribute<Termin, String> flags;
    public static volatile SingularAttribute<Termin, Integer> treatmentReason;
    public static volatile SingularAttribute<Termin, String> bereich;
    public static volatile SingularAttribute<Termin, String> dauer;
    public static volatile SingularAttribute<Termin, String> lastedit;
    public static volatile SingularAttribute<Termin, Integer> insuranceType;
    public static volatile SingularAttribute<Termin, Integer> priority;
    public static volatile SingularAttribute<Termin, String> terminStatus;
    public static volatile SingularAttribute<Termin, Integer> caseType;
    public static volatile SingularAttribute<Termin, String> statusHistory;
    public static volatile SingularAttribute<Termin, String> angelegt;
    public static volatile SingularAttribute<Termin, String> linkgroup;
    public static volatile SingularAttribute<Termin, LocalDate> tag;
    public static volatile SingularAttribute<Termin, String> beginn;

}