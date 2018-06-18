package polla.mt.model.dao;


import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import polla.mt.model.entity.Dependencia;
import polla.mt.model.entity.Pollero;

import java.util.List;

@Repository
@Transactional
public class DependenciaDao {

    @Autowired
    private SessionFactory sessionFactory;


    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Dependencia> obtenerListadoPosiciones() {
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(Dependencia.class);
        criteria.addOrder(Order.asc("posicion"));
        return criteria.list();
    }

    public void actualizarDependencia(Dependencia dependencia) {
        getSessionFactory().getCurrentSession().update(dependencia);
    }


    public void calcularPuntuacion(Dependencia dependencia) {
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(Pollero.class);
        criteria.add(Restrictions.eq("dependencia",dependencia.getNombre()));
        criteria.add(Restrictions.ne("puntuacion",0));
        criteria.setProjection(Projections.avg("puntuacion"));

        List listAvgSalary = criteria.list();
        Double avgSalary = (Double)listAvgSalary.get(0);
        dependencia.setPuntuacion(avgSalary);

        getSessionFactory().getCurrentSession().update(dependencia);

    }
}
