package com.tka.sams.api.dao;

import com.tka.sams.api.entity.User;
import com.tka.sams.api.model.LoginRequest;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDao {

    @Autowired
    private SessionFactory factory;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public User loginUser(LoginRequest request) {
        Session session = null;
        User user = null;
        try {
            session = factory.openSession();
            user = session.get(User.class, request.getUsername());

            if (user != null && passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                return user;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return null;
    }

    public User registerUser(User user) {
        Session session = null;
        try {
            session = factory.openSession();
            User existingUser = session.get(User.class, user.getUsername());
            if (existingUser == null) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                session.save(user);
                session.beginTransaction().commit();
                return user;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return null;
    }

    public String deleteUserById(String username) {
        Session session = null;
        String msg = null;
        try {
            session = factory.openSession();
            User user = session.get(User.class, username);
            session.delete(user);
            session.beginTransaction().commit();
            msg = "User Deleted Successfully!!";
        } catch (Exception e) {
            msg = null;
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return msg;
    }

    public User updateUser(User user) {
        Session session = null;

        try {
            session = factory.openSession();
            session.update(user);
            session.beginTransaction().commit();
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public List<User> getAllUser() {
        Session session = null;
        List<User> list = null;
        try {
            session = factory.openSession();
            Criteria criteria = session.createCriteria(User.class);
            list = criteria.list();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return list;
    }

    public User getUserByName(String username) {
        Session session = null;
        User user = null;
        try {
            session = factory.openSession();
            user = session.get(User.class, username);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return user;
    }

    public List<User> getAllAdmins() {
        Session session = null;
        List<User> list = null;
        try {
            session = factory.openSession();
            Criteria criteria = session.createCriteria(User.class);
            criteria.add(Restrictions.eq("role", "admin"));
            list = criteria.list();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return list;
    }

    public List<User> getAllFaculties() {
        Session session = null;
        List<User> list = null;
        try {
            session = factory.openSession();
            Criteria criteria = session.createCriteria(User.class);
            criteria.add(Restrictions.eq("role", "faculty"));
            list = criteria.list();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return list;
    }

}
