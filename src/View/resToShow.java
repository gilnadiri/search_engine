package View;

import javafx.beans.property.SimpleStringProperty;

public class resToShow {
    public final SimpleStringProperty docNo;
    public final SimpleStringProperty queryId;
    public final SimpleStringProperty entities;


    public resToShow(String docNo,String queryId,String entities) {
        this.docNo = new SimpleStringProperty(docNo);
        this.queryId = new SimpleStringProperty(queryId);
        this.entities = new SimpleStringProperty(entities);
    }

    public String getEntities() {
        return entities.get();
    }

    public SimpleStringProperty entitiesProperty() {
        return entities;
    }

    public void setEntities(String entities) {
        this.entities.set(entities);
    }


    public String getDocNo() {
        return docNo.get();
    }

    public SimpleStringProperty docNoProperty() {
        return docNo;
    }

    public void setDocNo(String docNo) {
        this.docNo.set(docNo);
    }

    public String getQueryId() {
        return queryId.get();
    }

    public SimpleStringProperty queryIdProperty() {
        return queryId;
    }

    public void setQueryId(String queryId) {
        this.queryId.set(queryId);
    }



}