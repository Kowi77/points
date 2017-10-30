package kov.develop.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.ListDataProvider;
import kov.develop.client.ui.WidgetPanel;
import kov.develop.shared.PointResult;
import kov.develop.shared.PointType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 *     Entry point class
**/
public class GwtApp implements EntryPoint {

    //Main Table Panel
    final VerticalPanel mainPanel = new VerticalPanel();
    //Choice Panel
    final HorizontalPanel choicePanel = new HorizontalPanel();
    //Full cache data
    List<PointResult> pointsList;
    //Filtered cache data
    List<PointResult> filteredList;
    //Drop boxes panels
    final WidgetPanel typePanel = new WidgetPanel(Arrays.asList(PointType.values()).stream().map(t -> t.toString()).collect(Collectors.toSet()));
    final WidgetPanel countryPanel = new WidgetPanel(new HashSet<String>());
    final WidgetPanel sityPanel = new WidgetPanel(new HashSet<String>());

    private final GwtAppServiceAsync gwtAppService = GWT.create(GwtAppService.class);

    //Fill table with dynamic loading data
    private ListDataProvider<PointResult> fillTable(ListDataProvider<PointResult> dataProvider) {
        this.gwtAppService.getAllPoints(new AsyncCallback<List<PointResult>>() {
            @Override
            public void onFailure(Throwable throwable) {
                GWT.log("Ошибка при загрузке списка", throwable);
            }

            @Override
            public void onSuccess(List<PointResult> points) {
                pointsList = new ArrayList<>(points);
                dataProvider.getList().addAll(points);
                refreshChoicePanel(pointsList);
            }
        });
        return dataProvider;
    }

    //Fill table with dynamic loading data by type
    private ListDataProvider<PointResult> fillTableByType(PointType type, ListDataProvider<PointResult> dataProvider) {
        this.gwtAppService.getAllPointsByType(type, new AsyncCallback<List<PointResult>>() {
            @Override
            public void onFailure(Throwable throwable) {
                GWT.log("Ошибка при загрузке списка", throwable);
            }

            @Override
            public void onSuccess(List<PointResult> points) {
                filteredList = new ArrayList<>(points);
                dataProvider.getList().addAll(points);
                RootPanel.get().add(new HTML("In method:  " + dataProvider.getList().size()));
                refreshChoicePanel(filteredList);
            }
        });
        return dataProvider;
    }

    //Fill table with dynamic loading data by Type and Country
    private ListDataProvider<PointResult> fillTableByTypeAndCountry(String type, String country, ListDataProvider<PointResult> dataProvider) {
        this.gwtAppService.getAllPointsByTypeAndCountry(type, country, new AsyncCallback<List<PointResult>>() {
            @Override
            public void onFailure(Throwable throwable) {
                GWT.log("Ошибка при загрузке списка", throwable);
            }

            @Override
            public void onSuccess(List<PointResult> points) {
                filteredList = new ArrayList<>(points);
                dataProvider.getList().addAll(points);
                refreshChoicePanel(filteredList);
            }
        });
        return dataProvider;
    }

    //Fill table with dynamic loading data by Type and Country and Sity
    private ListDataProvider<PointResult> fillTableByTypeAndCountryAndSity(String type, String country, String sity, ListDataProvider<PointResult> dataProvider) {
        this.gwtAppService.getAllPointsByTypeAndCountryAndSity(type, country, sity, new AsyncCallback<List<PointResult>>() {
            @Override
            public void onFailure(Throwable throwable) {
                GWT.log("Ошибка при загрузке списка", throwable);
            }

            @Override
            public void onSuccess(List<PointResult> points) {
                filteredList = new ArrayList<>(points);
                dataProvider.getList().addAll(points);
                refreshChoicePanel(filteredList);
            }
        });
        return dataProvider;
    }




    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {

        // Set main elements
        RootPanel.get("choicePanelContainer").add(choicePanel);
        RootPanel.get("mainPanelContainer").add(mainPanel);

        // Create table
        CellTable<PointResult> table = new CellTable<PointResult>();
        ListDataProvider<PointResult> dataProvider = GwtUtil.createTable(table);

        //Fill table
        fillTable(dataProvider);
        mainPanel.add(table);

        // Fill choicePanel
      //  choicePanel.setSpacing(5);
      //  choicePanel.add(typePanel.getListBox());
        //countryPanel.refreshPanel(pointsList.stream().map(p -> p.getCountry()).collect(Collectors.toSet()));
       // choicePanel.add(countryPanel);
       // sityPanel.refreshPanel(pointsList.stream().map(p -> p.getSity()).collect(Collectors.toSet()));
       // choicePanel.add(sityPanel);

        //Type handler
        typePanel.getListBox().addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                String item = typePanel.getListBox().getSelectedItemText();
                mainPanel.clear();
                CellTable<PointResult> table = new CellTable<PointResult>();
                ListDataProvider<PointResult> dataProvider = GwtUtil.createTable(table);
                if (item.equals("")){
                    fillTable(dataProvider);
                }
                else {
                    PointType type = PointType.valueOf(item);
                    fillTableByType(type, dataProvider);
                }
                 mainPanel.add(table);
            }
        });
        //Country handler
        countryPanel.getListBox().addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                String typeItem = typePanel.getListBox().getSelectedItemText();
                String countryItem = countryPanel.getListBox().getSelectedItemText();
                mainPanel.clear();
                CellTable<PointResult> table = new CellTable<PointResult>();
                ListDataProvider<PointResult> dataProvider = GwtUtil.createTable(table);
                if (countryItem.equals("") && typeItem.equals("")){
                    fillTable(dataProvider);
                }
                else if (countryItem.equals("")){
                    fillTableByType(PointType.valueOf(typeItem), dataProvider);
                }
                else {
                    fillTableByTypeAndCountry(typeItem, countryItem, dataProvider);
                }
                mainPanel.add(table);
            }
        });

        //Country handler
        sityPanel.getListBox().addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                String typeItem = typePanel.getListBox().getSelectedItemText();
                String countryItem = countryPanel.getListBox().getSelectedItemText();
                mainPanel.clear();
                CellTable<PointResult> table = new CellTable<PointResult>();
                ListDataProvider<PointResult> dataProvider = GwtUtil.createTable(table);
                if (countryItem.equals("") && typeItem.equals("")){
                    fillTable(dataProvider);
                }
                else if (countryItem.equals("")){
                    fillTableByType(PointType.valueOf(typeItem), dataProvider);
                }
                else {
                    fillTableByTypeAndCountry(typeItem, countryItem, dataProvider);
                }
                mainPanel.add(table);
            }
        });
    }



    public void refreshChoicePanel(List<PointResult> points){
        choicePanel.clear();
        choicePanel.add(typePanel.getListBox());
        countryPanel.refreshPanel(points.stream().map(p -> p.getCountry()).collect(Collectors.toSet()));
        sityPanel.refreshPanel(points.stream().map(p -> p.getSity()).collect(Collectors.toSet()));
        choicePanel.add(countryPanel);
        choicePanel.add(sityPanel);
    }
}

