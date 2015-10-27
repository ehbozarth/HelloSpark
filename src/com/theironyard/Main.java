package com.theironyard;

import spark.ModelAndView;
import spark.Session;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
        HashMap<String,User> users = new HashMap<>();
        Spark.staticFileLocation("/public");
        Spark.init();
        Spark.post(
                "/create-account",
                ((request, response) -> {

                    String name = request.queryParams("userName");
                    String password = request.queryParams("password");

                    Session session = request.session();
                    session.attribute("userName", name);

                    if (users.get(name) == null) {
                        User user = new User();
                        user.name = name;
                        user.password = password;
                        users.put(name, user);
                    }

                    response.redirect("/");
                    return "";
                })
        );//End of Spark.post()
/*
        Spark.get(
                "/accounts",
                ((request, response) -> {
                    HashMap m = new HashMap();
                    m.put("count", users.size());
                    m.put("accounts", users.values());
                    return new ModelAndView(m, "logged-in.html");
                }),
                new MustacheTemplateEngine()
        );//End of Spark.get() /Accounts
*/
        Spark.get(
                "/",
                ((request, response) -> {
                    Session session = request.session();
                    String name = session.attribute("userName");
                    if(name != null){
                        HashMap m = new HashMap();
                        m.put("count", users.size());
                        m.put("accounts", users.values());
                        return new ModelAndView(m, "logged-in.html");
                    }
                    HashMap m = new HashMap();
                    return new ModelAndView(m, "not-logged-in.html");
                }),
                new MustacheTemplateEngine()
        );//End of Spark.get() /

        Spark.post(
                "/logout",
                ((request, response) -> {
                    Session session = request.session();
                    session.invalidate();
                    response.redirect("/");
                    return "";
                })
        );

    }//End of Main method

}//End of Main Class
