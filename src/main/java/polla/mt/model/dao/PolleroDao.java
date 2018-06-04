package polla.mt.model.dao;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import polla.mt.model.entity.Pollero;

import java.util.List;

@Repository
@Transactional
public class PolleroDao {

    @Autowired
    private SessionFactory sessionFactory;


    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Pollero> obtenerListadoPosiciones() {
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(Pollero.class);
        criteria.addOrder(Order.asc("posicion"));
        return criteria.list();
    }

    public Pollero buscarPollero(Integer codigo) {
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(Pollero.class);
        criteria.add(Restrictions.eq("codigo",codigo));
        return (Pollero) criteria.uniqueResult();
    }

    public void guardarPollero(Pollero pollero) {
        getSessionFactory().getCurrentSession().save(pollero);
    }

    public void actualizarPollero(Pollero pollero) {
        getSessionFactory().getCurrentSession().update(pollero);
    }

    public List<Pollero> obtenerPollerosPorPuntaje() {
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(Pollero.class);
        criteria.addOrder(Order.desc("puntuacion"));
        return criteria.list();
    }
}
