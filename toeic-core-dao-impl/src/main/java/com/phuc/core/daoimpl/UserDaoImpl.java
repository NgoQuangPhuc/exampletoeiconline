package com.phuc.core.daoimpl;

import com.phuc.core.common.utils.HibernateUtil;
import com.phuc.core.dao.UserDao;
import com.phuc.core.data.daoimpl.AbstractDao;
import com.phuc.core.persistence.entity.RoleEntity;
import com.phuc.core.persistence.entity.UserEntity;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;


public class UserDaoImpl extends AbstractDao<Integer, UserEntity> implements UserDao {

    public Object[] checkLogin(String name, String password) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        boolean isUserExist = false;
        String roleName = null;
        try {
            StringBuilder sql = new StringBuilder(" FROM UserEntity ue WHERE ue.name= :name AND ue.password= :password");
            Query query = session.createQuery(sql.toString());
            query.setParameter("name", name);
            query.setParameter("password", password);
            if (query.list().size() >0) {
                isUserExist = true;
                UserEntity userEntity = (UserEntity) query.uniqueResult();
                roleName = userEntity.getRoleEntity().getName();
            }
        } catch (HibernateException e) {
            transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
        return new Object[]{isUserExist, roleName};
    }
}
