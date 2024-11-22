package com.noize.medicalcenter.controller;

import com.jfoenix.controls.JFXTextField;
import com.noize.medicalcenter.db.DBConnection;
import com.noize.medicalcenter.dto.ItemFormDto;
import com.noize.medicalcenter.dto.tm.ItemTM;
import com.noize.medicalcenter.model.ItemFormModel;
import com.noize.medicalcenter.util.AlertNotification;
import com.noize.medicalcenter.util.CheckRegex;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class ItemFormController implements Initializable {
    ItemFormModel itemFormModel = new ItemFormModel();
    Boolean isNameValid = false;
    Boolean isPackSizeValid = false;
    Boolean isQtyValid = false;
    Boolean isPriceValid = false;
    Boolean isExpireValid = false;
    Boolean isDescValid = false;

    @FXML
    private Button btnDeleteItem;

    @FXML
    private AnchorPane addPane;

    @FXML
    private TextField lblSearch;

    @FXML
    private AnchorPane updatePane;

    @FXML
    private Button btnReset;

    @FXML
    private Button btnSaveItem;

    @FXML
    private Button btnUpdateItem;

    @FXML
    private ComboBox<String> comboMovement;

    @FXML
    private ComboBox<String> comboName;

    @FXML
    private TableColumn<ItemTM, String> descCol;

    @FXML
    private TableColumn<ItemTM, String> expireDateCol;

    @FXML
    private TableColumn<ItemTM, String> namCol;

    @FXML
    private TableColumn<ItemTM, String> packSizeCol;

    @FXML
    private TableColumn<ItemTM, Integer> stockQtyCol;

    @FXML
    private TableView<ItemTM> tblItem;

    @FXML
    private JFXTextField txtDesc;

    @FXML
    private DatePicker expirePicker;

    @FXML
    private JFXTextField txtMediName;

    @FXML
    private JFXTextField txtPackSize;

    @FXML
    private JFXTextField txtQty;

    @FXML
    private JFXTextField txtQtyUpdate;

    @FXML
    private JFXTextField txtUPrice;

    @FXML
    private ImageView back;

    @FXML
    private JFXTextField txtUPriceUpdate;

    @FXML
    private TableColumn<?, ?> typeCol;

    @FXML
    private TableColumn<ItemTM, Double> unitPriceCol;

    @FXML
    void btnDeleteItemOnAction(ActionEvent event) throws SQLException {
        if (tblItem != null) {
            ItemTM selectedItem = tblItem.getSelectionModel().getSelectedItem();
            if (selectedItem == null) {
                new AlertNotification(
                        "Error Message",
                        "Please select an item to delete from the table.",
                        "unsuccess.png",
                        "ok"
                ).start();
                return;
            }
        }
        String ItemName = tblItem.getSelectionModel().getSelectedItem().getName();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this Item ?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> buttonType = alert.showAndWait();

        if (buttonType.get() == ButtonType.YES) {
            clearFields();
            refreshTable();
            boolean isDeleted = itemFormModel.deleteItem(ItemName);
            if (isDeleted) {
                new AlertNotification(
                        "Success Message",
                        "Item deleted successfully !",
                        "success.png",
                        "ok"
                ).start();
                refreshTable();
            } else {
                new AlertNotification(
                        "Error Message",
                        "An error occurred while deleting the item. Please try again later or contact support.",
                        "unsuccess.png",
                        "ok"
                ).start();
            }
        }else {
            new AlertNotification(
                    "Error",
                    "You have canceled the delete operation.",
                    "unsuccess.png",
                    "ok"
            ).start();
        }
    }

    @FXML
    void btnResetOnAction(ActionEvent event) {
        txtMediName.clear();
        txtPackSize.clear();
        expirePicker.setValue(null);
        txtDesc.clear();
        txtQty.clear();
        txtUPrice.clear();
        btnSaveItem.setDisable(false);
        btnUpdateItem.setDisable(true);
        btnDeleteItem.setDisable(true);

        txtMediName.setStyle("");
        txtPackSize.setStyle("");
        expirePicker.setStyle("");
        txtDesc.setStyle("");
        txtQty.setStyle("");
        txtUPrice.setStyle("");

        isNameValid = false;
        isPackSizeValid = false;
        isQtyValid = false;
        isPriceValid = false;
        isExpireValid = false;
        isDescValid = false;


    }

    private void clearFields() {
        txtMediName.clear();
        txtPackSize.clear();
        txtQty.clear();
        txtUPrice.clear();
        txtDesc.clear();
        expirePicker.setValue(null);
    }

    @FXML
    void btnSaveItemOnAction(ActionEvent event) {
        isExpireValid = expirePicker.getValue() != null;

        if (isExpireValid && !isNameValid && !isPackSizeValid && !isQtyValid && !isPriceValid && !isDescValid) {
            new AlertNotification(
                    "Error Message",
                    "Please check the input fields and try again.",
                    "unsuccess.png",
                    "ok"
            ).start();
            return;
        }
        String name = txtMediName.getText();
        String packSize = txtPackSize.getText();
        LocalDate expire = expirePicker.getValue();
        String description = txtDesc.getText();
        int qty = Integer.parseInt(txtQty.getText());
        double price = Double.parseDouble(txtUPrice.getText());

        if (itemFormModel.isAddStock(name,description, String.valueOf(expire),packSize,price,qty)) {
            getStockQty();
            new AlertNotification(
                    "Success Message",
                    "Item added successfully to item list",
                    "success.png",
                    "ok"
            ).start();
            getStockQty();
            clearFields();
            refreshTable();
        } else {
            new AlertNotification(
                    "Error Message",
                    "Failed to add to item list",
                    "unsuccess.png",
                    "ok"
            ).start();
        }
    }

    @FXML
    void btnUpdateItemOnAction(ActionEvent event) throws SQLException {
        isExpireValid = expirePicker.getValue() != null;

        String name = txtMediName.getText();
        String packSize = txtPackSize.getText();
        LocalDate expire = expirePicker.getValue();
        String description = txtDesc.getText();
        int qty = Integer.parseInt(txtQty.getText());
        double price = Double.parseDouble(txtUPrice.getText());

            ItemFormDto itemFormDto = new ItemFormDto(
                    0,
                    name,
                    description,
                    String.valueOf(expire),
                    packSize,
                    price,
                    qty
            );
            boolean isUpdate = itemFormModel.isUpdateStock(itemFormDto);

            if (isUpdate){
                refreshTable();
                clearFields();
                new AlertNotification(
                        "Success Message",
                        "Item updated successfully to item list",
                        "success.png",
                        "ok"
                ).start();
            }else {
                new AlertNotification(
                        "Error Message",
                        "Failed to update to item list",
                        "unsuccess.png",
                        "ok"
                ).start();
            }
    }

    @FXML
    void backOnClicked(MouseEvent event) {
    }

    @FXML
    void descReleased(KeyEvent event) {
        if (CheckRegex.checkRegex("desc", txtDesc.getText())) {
            txtDesc.setStyle("-fx-text-fill: green;");
            isDescValid = true;
        } else {
            txtDesc.setStyle("-fx-text-fill: red;");
            isDescValid = false;
        }
    }

    @FXML
    void onClickTable(MouseEvent event) {
        ItemTM selectItem = tblItem.getSelectionModel().getSelectedItem();
        if (selectItem != null) {
            txtMediName.setText(selectItem.getName());
            txtQty.setText(String.valueOf(selectItem.getStockQty()));
            txtUPrice.setText(String.valueOf(selectItem.getUnitPrice()));
            txtPackSize.setText(selectItem.getPackSize());
            txtDesc.setText(selectItem.getDescription());
            expirePicker.setValue(LocalDate.parse(selectItem.getExpireDate()));

            btnSaveItem.setDisable(true);
            btnUpdateItem.setDisable(false);
            btnDeleteItem.setDisable(false);
        }
    }

    @FXML
    void mediNameReleased(KeyEvent event) {
        if (CheckRegex.checkRegex("name", txtMediName.getText())) {
            txtMediName.setStyle("-fx-text-fill: green;");
            isNameValid = true;
        } else {
            txtMediName.setStyle("-fx-text-fill: red;");
            isNameValid = false;
        }
    }

    @FXML
    void packReleased(KeyEvent event) {
        if (CheckRegex.checkRegex("packSize", txtPackSize.getText())) {
            txtPackSize.setStyle("-fx-text-fill: green;");
            isPackSizeValid = true;
        } else {
            txtPackSize.setStyle("-fx-text-fill: red;");
            isPackSizeValid = false;
        }
    }

    @FXML
    void qtyReleased(KeyEvent event) {
        if (CheckRegex.checkRegex("qty", txtQty.getText())) {
            txtQty.setStyle("-fx-text-fill: green;");
            isQtyValid = true;
        } else {
            txtQty.setStyle("-fx-text-fill: red;");
            isQtyValid = false;
        }
    }

    @FXML
    void uPriceReleased(KeyEvent event) {
        if (CheckRegex.checkRegex("price", txtUPrice.getText())) {
            txtUPrice.setStyle("-fx-text-fill: green;");
            isPriceValid = true;
        } else {
            txtUPrice.setStyle("-fx-text-fill: red;");
            isPriceValid = false;
        }
    }

    @FXML
    void updatePricereleased(KeyEvent event) {

    }

    @FXML
    void updateQtyReleased(KeyEvent event) {

    }

    @FXML
    void refreshTable() {
        tblItem.getItems().clear();
        ArrayList<ItemTM> allItems = itemFormModel.getStock();
        ObservableList<ItemTM> itemsList = FXCollections.observableArrayList(allItems);
        tblItem.setItems(itemsList);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        namCol.setCellValueFactory(new PropertyValueFactory<>("Name"));
        packSizeCol.setCellValueFactory(new PropertyValueFactory<>("PackSize"));
        unitPriceCol.setCellValueFactory(new PropertyValueFactory<>("UnitPrice"));
        stockQtyCol.setCellValueFactory(new PropertyValueFactory<>("StockQty"));
        expireDateCol.setCellValueFactory(new PropertyValueFactory<>("ExpireDate"));
        descCol.setCellValueFactory(new PropertyValueFactory<>("Description"));

        try {
            getItems();
            getStockQty();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void getStockQty() {
        ArrayList<ItemTM> itemsDtos = itemFormModel.getStock();
        tblItem.getItems().clear();
        tblItem.getItems().addAll(itemsDtos);
    }

    public void getItems() throws SQLException {
        ArrayList<String> items = itemFormModel.getAllItemNames();
        comboName.getItems().clear();
        comboName.getItems().addAll(items);

        ArrayList<String> movements = new ArrayList<>();
        movements.add("Stock In");
        movements.add("Stock Out");
        comboMovement.getItems().clear();
        comboMovement.getItems().addAll(movements);
    }

    public void expirePicker(ActionEvent actionEvent) {
            expirePicker.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: green;");
            LocalDate expire = expirePicker.getValue();
            if (expire != null && expire.isBefore(LocalDate.now())) {
                isExpireValid = true;
            } else {
                isExpireValid = false;
            }
    }

    public void searchStock(KeyEvent keyEvent) throws SQLException {
        ArrayList<ItemTM> items = itemFormModel.searchStock(lblSearch.getText());
        ObservableList<ItemTM> itemTMS = FXCollections.observableArrayList();
        for (ItemTM item : items) {
            itemTMS.add(item);
        }
        tblItem.setItems(itemTMS);
    }

    public void reportOnAction(ActionEvent event) {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("TODAY", LocalDate.now().toString());
            JasperReport jasperReport = JasperCompileManager.compileReport(
                    getClass().getResourceAsStream("/reports/Item_Report.jrxml")
            );
            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    jasperReport,
                    parameters,
                    connection
            );
            JasperViewer.viewReport(jasperPrint, false);

        } catch (JRException e) {
            new AlertNotification(
                    "Error Message",
                    "An error occurred while loading the report. Please try again later.",
                    "unsuccess.png",
                    "OK"
            ).start();
            e.printStackTrace();
        } catch (SQLException e) {
            new AlertNotification(
                    "Error Message",
                    "Data empty. Please try again later.",
                    "unsuccess.png",
                    "OK"
            ).start();
            e.printStackTrace();
        }
    }
}
