package info.elexis.server.core.connector.elexis.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import info.elexis.server.core.connector.elexis.billable.VerrechenbarArtikelstammItem;
import info.elexis.server.core.connector.elexis.billable.VerrechenbarLabor2009Tarif;
import info.elexis.server.core.connector.elexis.billable.VerrechenbarPhysioLeistung;
import info.elexis.server.core.connector.elexis.billable.VerrechenbarTarmedLeistung;
import info.elexis.server.core.connector.elexis.internal.BundleConstants;
import info.elexis.server.core.connector.elexis.jpa.model.annotated.AbstractDBObjectIdDeleted;
import info.elexis.server.core.connector.elexis.jpa.model.annotated.ArtikelstammItem;
import info.elexis.server.core.connector.elexis.jpa.model.annotated.Behandlung;
import info.elexis.server.core.connector.elexis.jpa.model.annotated.ConsultationDiagnosis;
import info.elexis.server.core.connector.elexis.jpa.model.annotated.Diagnosis;
import info.elexis.server.core.connector.elexis.jpa.model.annotated.Fall;
import info.elexis.server.core.connector.elexis.jpa.model.annotated.Kontakt;
import info.elexis.server.core.connector.elexis.jpa.model.annotated.Labor2009Tarif;
import info.elexis.server.core.connector.elexis.jpa.model.annotated.PhysioLeistung;
import info.elexis.server.core.connector.elexis.jpa.model.annotated.TarmedLeistung;

public class BehandlungService extends AbstractService<Behandlung> {

	public static BehandlungService INSTANCE = InstanceHolder.INSTANCE;

	private static final class InstanceHolder {
		static final BehandlungService INSTANCE = new BehandlungService();
	}

	private BehandlungService() {
		super(Behandlung.class);
	}

	/**
	 * Create a {@link Behandlung} with mandatory attributes
	 * 
	 * @param fall
	 * @param mandator
	 * @return
	 */
	public Behandlung create(Fall fall, Kontakt mandator) {
		em.getTransaction().begin();
		Behandlung cons = create(false);
		em.merge(fall);
		em.merge(mandator);
		cons.setDatum(LocalDate.now());
		cons.setFall(fall);
		cons.setMandant(mandator);
		// TODO fall.getPatient().setInfoElement("LetzteBehandlung", getId());
		em.getTransaction().commit();
		return cons;
	}

	public static IStatus chargeBillableOnBehandlung(Behandlung kons, AbstractDBObjectIdDeleted billableObject,
			Kontakt userContact, Kontakt mandatorContact) {
		if (billableObject instanceof TarmedLeistung) {
			return new VerrechenbarTarmedLeistung((TarmedLeistung) billableObject).add(kons, userContact,
					mandatorContact);
		} else if (billableObject instanceof Labor2009Tarif) {
			return new VerrechenbarLabor2009Tarif((Labor2009Tarif) billableObject).add(kons, userContact,
					mandatorContact);
		} else if (billableObject instanceof PhysioLeistung) {
			return new VerrechenbarPhysioLeistung((PhysioLeistung) billableObject).add(kons, userContact,
					mandatorContact);
		} else if (billableObject instanceof ArtikelstammItem) {
			return new VerrechenbarArtikelstammItem((ArtikelstammItem) billableObject).add(kons, userContact,
					mandatorContact);
		}

		return new Status(Status.ERROR, BundleConstants.BUNDLE_ID,
				"No Verrechenbar wrapper found for " + billableObject.getLabel());
	}

	/**
	 * 
	 * @param patient
	 * @return all {@link Behandlung} for patient, ordered by date (newest
	 *         first)
	 */
	public static List<Behandlung> findAllConsultationsForPatient(Kontakt patient) {
		// TODO create a single mysql join statement
		List<Fall> faelle = patient.getFaelle();
		List<Behandlung> collect = faelle.stream().flatMap(f -> f.getConsultations().stream())
				.sorted((c1, c2) -> c2.getDatum().compareTo(c1.getDatum())).collect(Collectors.toList());
		return collect;
	}

	/**
	 * Set a specific diagnosis on a consultation
	 * @param cons
	 * @param diag
	 */
	public void setDiagnosisOnConsultation(Behandlung cons, Diagnosis diag) {
		ConsultationDiagnosis cdj = new ConsultationDiagnosis();
		cdj.setConsultation(cons);
		cdj.setDiagnosis(diag);
		
		Set<ConsultationDiagnosis> diagnoses = cons.getDiagnoses();
		for (ConsultationDiagnosis cd : diagnoses) {
			Diagnosis diagnosis = cd.getDiagnosis();
			if(diagnosis.getCode().equals(diag.getCode()) && diagnosis.getDiagnosisClass().equals(diag.getDiagnosisClass())) {
				return;
			}
		}

		cons.getDiagnoses().add(cdj);
		BehandlungService.INSTANCE.flush();
	}
	
}
