/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imagebrowser.controller;

import imagebrowser.model.FolderImages;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;

import javax.imageio.ImageIO;

/**
 *
 * @author Sely
 */
public class FXMLImageBrowserController implements Initializable {

    static double imageX = 0;
    static double imageY = 0;
    private File directory = null;

    @FXML
    private Label labelBrowseFolder;
    @FXML
    private ImageView imageViewContainer = new ImageView();
    @FXML
    private MenuItem menuNext;
    @FXML
    private MenuItem menuPrevious;
    @FXML
    ComboBox comboBox;
    @FXML
    Button buttonGo;
    @FXML
    BorderPane borderPane;
    @FXML
    Button buttonSlideShow;
    @FXML
    ListView listView; 
    @FXML 
    VBox imageContainer= new VBox();
    
    @FXML    
    private void handleButtonAction(ActionEvent event) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Browse Folder");
        File selectedDirectory = chooser.showDialog(null);

        if (selectedDirectory != null && selectedDirectory.isDirectory()) {
            labelBrowseFolder.setText(selectedDirectory.getAbsolutePath());
            directory = selectedDirectory;
        }

        loadImage();
    }

    @FXML
    private void handleButtonPreviousAction(ActionEvent event) {
        moveToPreviousImage();
    }

    @FXML
    private void handleButtonNextAction(ActionEvent event) {
        moveToNextImage();
    }

    @FXML
    private void handleMenuPreviousAction(ActionEvent event) {
        moveToPreviousImage();
    }

    @FXML
    private void handleMenuNextAction(ActionEvent event) {
        moveToNextImage();
    }

    @FXML
    private void handleKeyPressedAction(KeyEvent event) {
        if (event.getCode() == KeyCode.RIGHT) {
            moveToNextImage();
        } else if (event.getCode() == KeyCode.LEFT) {
            moveToPreviousImage();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO                    
        Label labelNextM = new Label("Next");
        Label labelPreviousM = new Label("Previous");
        labelNextM.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                moveToNextImage();
            }
        });
        menuNext.setGraphic(labelNextM);

        labelPreviousM.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                moveToPreviousImage();
            }
        });
        menuPrevious.setGraphic(labelPreviousM);

        //Set image event listener                 
        imageViewContainer.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton() == MouseButton.PRIMARY) {
                    moveToPreviousImage();
                } else if (event.getButton() == MouseButton.SECONDARY) {
                    moveToNextImage();
                }
            }
        });

        //Adding items to combo box
        comboBox.getItems().add("Previous");
        comboBox.getItems().add("Next");

        buttonGo.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				try {
                    String data = comboBox.getSelectionModel().getSelectedItem().toString();
                    if (data.equals("Previous")) {
                        moveToPreviousImage();
                    } else if (data.equals("Next")) {
                        moveToNextImage();
                    }

                } catch (Exception e) {

                }				
			}
		});

        buttonSlideShow.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						while(FolderImages.getIndex()<FolderImages.getImageFiles().size()-1){
							moveToNextImage();					
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								
							}
						}
					}
				}).start();
											
			}
		});
        
        //listview listener
        listView.setOnMouseClicked(new EventHandler<Event>() {

			@Override
			public void handle(Event arg0) {
				int index =listView.getSelectionModel().getSelectedIndex();
				setCurrentImage(index);
			}
		});
        
        imageViewContainer.fitHeightProperty().bind(imageContainer.heightProperty());
        imageViewContainer.fitWidthProperty().bind(imageContainer.widthProperty());
        //imageViewContainer.setPreserveRatio(true);
        
    }
    void loadImage() {
        File[] directoryListing = directory.listFiles();
        FolderImages.getImageFiles().clear();
        FolderImages.setIndex(-1);
        listView.getItems().clear();
        for (File child : directoryListing) {
            // Do something with child
            try {
                BufferedImage image = ImageIO.read(child);
                if (image == null) {
                    continue;
                }
                FolderImages.getImageFiles().add(child.getAbsolutePath());
              //Fill list view
                listView.getItems().add(child.getAbsolutePath());
            } catch (IOException ex) {
                continue;
            }
        }
        if (FolderImages.getImageFiles().size() > 0) {
            FolderImages.setIndex(0);

            displayImage(FolderImages.getImageFiles().get(FolderImages.getIndex()));
  
        }
    }

    void moveToNextImage() {
        if (FolderImages.getImageFiles().size() > 0) {
            if (!(FolderImages.getIndex() == FolderImages.getImageFiles().size() - 1)) {
                FolderImages.setIndex(FolderImages.getIndex() + 1);
                displayImage(FolderImages.getImageFiles().get(FolderImages.getIndex()));
            }
        }

    }

    void moveToPreviousImage() {
        if (FolderImages.getImageFiles().size() > 0) {
            if (FolderImages.getIndex() > 0) {
                FolderImages.setIndex(FolderImages.getIndex() - 1);
                displayImage(FolderImages.getImageFiles().get(FolderImages.getIndex()));
            }
        }

    }
    
    void setCurrentImage(int index) {
        if (FolderImages.getImageFiles().size() > 0 && index>=0 && index<FolderImages.getImageFiles().size()) {
        	FolderImages.setIndex(index);
            displayImage(FolderImages.getImageFiles().get(FolderImages.getIndex()));
        }

    }

    private void displayImage(String filePath) {
        Image im = new Image(new File(filePath).toURI().toString());
        imageViewContainer.setImage(im);       
        
    }
}
