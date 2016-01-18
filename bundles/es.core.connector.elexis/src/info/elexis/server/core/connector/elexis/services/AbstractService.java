package info.elexis.server.core.connector.elexis.services;

import static info.elexis.server.core.connector.elexis.internal.ElexisEntityManager.em;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import info.elexis.server.core.connector.elexis.internal.ElexisEntityManager;

public class AbstractService<T> {

	private final Class<T> clazz;

	public AbstractService(Class<T> clazz) {
		this.clazz = clazz;
	}

	public static <T> T findById(Object id, Class<T> entityClass) {
		return (T) em().find(entityClass, id);
	}

	public List<T> findAll(boolean includeElementsMarkedDeleted) {
		CriteriaBuilder qb = em().getCriteriaBuilder();
		CriteriaQuery<T> c = qb.createQuery(clazz);

		if (!includeElementsMarkedDeleted) {
			Root<T> r = c.from(clazz);
			Predicate like = qb.like(r.get("deleted"), "0");
			c = c.where(like);
		}

		TypedQuery<T> q = ElexisEntityManager.em().createQuery(c);
		return q.getResultList();
	};
}