package ru.doxhost.newhost.server.web.form;

import java.util.*;


public class FormValidation {

    private Map<String, String> map = new HashMap<>();

    public void add(String field, String message) {
        map.put(field, message);
    }

    public String getMessage(String field) {

        String msg = "";

        if (map.containsKey(field)) {
            msg = map.get(field);
        }

        return msg;
    }

    public int size() {
        return map.size();
    }

    /**
     *
     * @param asMap
     * @return
     */
    public Map<String, String> getErrors(boolean asMap) {
        return Collections.unmodifiableMap(map);
    }

    Set<FormValidationBean> errors = new HashSet<>();

    public Set<FormValidationBean> getErrors() {

        if (errors.size() == 0) {

            Collections.unmodifiableMap(map).entrySet().stream().forEach(entry-> {
                errors.add(new FormValidationBean(entry.getKey(), entry.getValue()));
            });
        }

        return errors;
    }

    public class FormValidationBean {

        private String field;
        private String message;

        public FormValidationBean(String field, String message) {
            this.field = field;
            this.message = message;
        }

        public String getField() {
            return field;
        }

        public String getMessage() {
            return message;
        }
    }
}
