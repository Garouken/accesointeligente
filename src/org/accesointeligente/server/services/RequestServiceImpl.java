package org.accesointeligente.server.services;

import org.accesointeligente.client.services.RequestService;
import org.accesointeligente.model.Request;
import org.accesointeligente.model.RequestCategory;
import org.accesointeligente.model.User;
import org.accesointeligente.server.HibernateUtil;
import org.accesointeligente.server.SessionUtil;
import org.accesointeligente.shared.RequestStatus;
import org.accesointeligente.shared.ServiceException;

import net.sf.gilead.core.PersistentBeanManager;
import net.sf.gilead.gwt.PersistentRemoteService;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class RequestServiceImpl extends PersistentRemoteService implements RequestService {
	private static final long serialVersionUID = -8965250779021980788L;
	private PersistentBeanManager persistentBeanManager;

	public RequestServiceImpl() {
		persistentBeanManager = HibernateUtil.getPersistentBeanManager();
		setBeanManager(persistentBeanManager);
	}

	@Override
	public Request makeRequest(Request request) throws ServiceException {
		Session hibernate = HibernateUtil.getSession();
		hibernate.beginTransaction();

		try {
			hibernate.save(request);
			hibernate.getTransaction().commit();
			return request;
		} catch (Throwable ex) {
			hibernate.getTransaction().rollback();
			throw new ServiceException();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RequestCategory> getCategories() throws ServiceException {
		Session hibernate = HibernateUtil.getSession();
		hibernate.beginTransaction();

		try {
			Criteria criteria = hibernate.createCriteria(RequestCategory.class);
			criteria.addOrder(Order.asc("id"));
			List<RequestCategory> categories = (List<RequestCategory>) persistentBeanManager.clone(criteria.list());
			hibernate.getTransaction().commit();
			return categories;
		} catch (Throwable ex) {
			hibernate.getTransaction().rollback();
			throw new ServiceException();
		}
	}

	@Override
	public Request getRequest(Integer requestId) throws ServiceException {
		Session hibernate = HibernateUtil.getSession();
		hibernate.beginTransaction();

		try {
			Criteria criteria = hibernate.createCriteria(Request.class);
			criteria.setFetchMode("user", FetchMode.JOIN);
			criteria.setFetchMode("institution", FetchMode.JOIN);
			criteria.setFetchMode("categories", FetchMode.JOIN);
			criteria.add(Restrictions.eq("id", requestId));
			Request request = (Request) criteria.uniqueResult();
			return (Request) persistentBeanManager.clone(request);
		} catch (Throwable ex) {
			hibernate.getTransaction().rollback();
			throw new ServiceException();
		}
	}

	@Override
	public List<Request> getUserRequestList(Integer offset, Integer limit) throws ServiceException {
		Session hibernate = HibernateUtil.getSession();
		hibernate.beginTransaction();

		try {
			User user = SessionUtil.getUser();
			Criteria criteria = hibernate.createCriteria(Request.class);
			criteria.setFirstResult(offset);
			criteria.setMaxResults(limit);
			criteria.add(Restrictions.eq("user", user));
			criteria.addOrder(Order.asc("date"));
			criteria.addOrder(Order.asc("institution"));
			criteria.setFetchMode("institution", FetchMode.JOIN);
			List<Request> requests = (List<Request>) persistentBeanManager.clone(criteria.list());
			return requests;
		} catch (Throwable ex) {
			hibernate.getTransaction().rollback();
			throw new ServiceException();
		}
	}

	@Override
	public List<Request> getUserFavoriteRequestList(Integer offset, Integer limit) throws ServiceException {
		Session hibernate = HibernateUtil.getSession();
		hibernate.beginTransaction();

		try {
			User user = SessionUtil.getUser();
			Criteria criteria = hibernate.createCriteria(Request.class);
			criteria.setFirstResult(offset);
			criteria.setMaxResults(limit);
			criteria.add(Restrictions.eq("user", user));
			criteria.addOrder(Order.asc("date"));
			criteria.addOrder(Order.asc("institution"));
			criteria.setFetchMode("institution", FetchMode.JOIN);
			List<Request> requests = (List<Request>) persistentBeanManager.clone(criteria.list());
			return requests;
		} catch (Throwable ex) {
			hibernate.getTransaction().rollback();
			throw new ServiceException();
		}
	}

	@Override
	public List<Request> getRequestList(Integer offset, Integer limit) throws ServiceException {
		Session hibernate = HibernateUtil.getSession();
		hibernate.beginTransaction();

		try {
			Criteria criteria = hibernate.createCriteria(Request.class);
			criteria.setFirstResult(offset);
			criteria.setMaxResults(limit);
			criteria.add(Restrictions.ne("status", RequestStatus.NEW));
			criteria.addOrder(Order.asc("date"));
			criteria.addOrder(Order.asc("institution"));
			criteria.setFetchMode("institution", FetchMode.JOIN);
			List<Request> requests = (List<Request>) persistentBeanManager.clone(criteria.list());
			return requests;
		} catch (Throwable ex) {
			hibernate.getTransaction().rollback();
			throw new ServiceException();
		}
	}
}
