package view;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import controller.GameController;
import controller.GameController.Status;
import controller.abilities.Ability;
import model.Card;
import model.CardTile;
import model.Energy;
import model.Player;
import model.Player.Role;
import model.Pokemon;
import model.Pokemon.Stage;
import model.Trainer;
import model.Energy.Category;
import view.CardLabel.Place;

import javax.swing.JLabel;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import java.awt.GridLayout;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class GameBoard extends JFrame implements Observer{

	private JPanel contentPane;
	private JLabel notificationLabel, cardInfoLabel;
	private GameController controller;
	private JPanel myHandPanel;
	private JPanel opHandPanel;
	private Timer timer;
	private JLabel ability_1label;
	private JLabel ability_2label;
	private JLabel retreatLabel;
	private JButton ability_1button;
	private JButton ability_2button;
	private JButton retreatButton;
	private JLabel opDeckCount;
	private JLabel myDeckCount;
	private JLabel opDiscardCount;
	private JLabel myDiscardCount;
	private JLabel status;
	

	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					GameBoard frame = new GameBoard();
//					frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}
	
	public GameBoard(GameController c){
		controller = c;
		init();
	}

	/**
	 * Create the frame.
	 */
	public void init() {
		
		//frame init
		setIconImage(Toolkit.getDefaultToolkit().getImage("src/images/ball.png"));
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 1150, 730);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		opHandPanel = new JPanel(new BorderLayout());
		opHandPanel.setBounds(195, 5, 733, 110);
		opHandPanel.setOpaque(false);
		opHandPanel.setBorder(new LineBorder(Color.BLACK, 1));
		contentPane.add(opHandPanel);
		
		myHandPanel = new JPanel(new BorderLayout());
		myHandPanel.setBounds(195, 585, 733, 110);
		myHandPanel.setOpaque(false);
		myHandPanel.setBorder(new LineBorder(Color.BLACK, 1));
		contentPane.add(myHandPanel);
		
		JButton btnDoneWithTurn = new JButton("Done with Turn");
		btnDoneWithTurn.setBounds(971, 642, 121, 37);
		contentPane.add(btnDoneWithTurn);
		btnDoneWithTurn.setBackground(new Color(255, 0, 0));
		btnDoneWithTurn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.doneWithTurn();
			}
		});
		
		JPanel opPenchPanel = new JPanel();
		opPenchPanel.setBounds(378, 122, 425, 104);
		contentPane.add(opPenchPanel);
		opPenchPanel.setBorder(new LineBorder(Color.BLACK,1));
		opPenchPanel.setLayout(null);
		opPenchPanel.setOpaque(false);
		
		CardLabel opBench1 = new CardLabel(Place.OP_BENCH, 1, controller);
		opBench1.setBounds(10, 4, 69, 95);
		opPenchPanel.add(opBench1);
		controller.getOpponent().getCardTile(Place.OP_BENCH, 1).addObserverLabel(opBench1);
		
		CardLabel opBench2 = new CardLabel(Place.OP_BENCH, 2, controller);
		opBench2.setBounds(93, 4, 69, 95);
		opPenchPanel.add(opBench2);
		controller.getOpponent().getCardTile(Place.OP_BENCH, 2).addObserverLabel(opBench2);
		
		CardLabel opBench3 = new CardLabel(Place.OP_BENCH, 3, controller);
		opBench3.setBounds(176, 4, 69, 95);
		opPenchPanel.add(opBench3);
		controller.getOpponent().getCardTile(Place.OP_BENCH, 3).addObserverLabel(opBench3);
		
		CardLabel opBench4 = new CardLabel(Place.OP_BENCH, 4, controller);
		opBench4.setBounds(259, 4, 69, 95);
		opPenchPanel.add(opBench4);
		controller.getOpponent().getCardTile(Place.OP_BENCH, 4).addObserverLabel(opBench4);
		
		CardLabel opBench5 = new CardLabel(Place.OP_BENCH, 5, controller);
		opBench5.setBounds(342, 4, 69, 95);
		opPenchPanel.add(opBench5);
		controller.getOpponent().getCardTile(Place.OP_BENCH, 5).addObserverLabel(opBench5);
		
		JPanel opPrizePanel = new JPanel();
		opPrizePanel.setBounds(10, 11, 172, 328);
		opPrizePanel.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0), 2, true), "Op-Prize cards", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 255)));
		opPrizePanel.setOpaque(false);
		contentPane.add(opPrizePanel);
		opPrizePanel.setLayout(null);
		
		CardLabel opPrize1 = new CardLabel(Place.OP_PRIZE, 1, controller);
		opPrize1.setBounds(10, 22, 69, 95);
		opPrizePanel.add(opPrize1);
		controller.getOpponent().getCardTile(Place.OP_PRIZE, 1).addObserverLabel(opPrize1);
		
		CardLabel opPrize2 = new CardLabel(Place.OP_PRIZE, 2, controller);
		opPrize2.setBounds(92, 22, 69, 95);
		opPrizePanel.add(opPrize2);
		controller.getOpponent().getCardTile(Place.OP_PRIZE, 2).addObserverLabel(opPrize2);
		
		CardLabel opPrize3 = new CardLabel(Place.OP_PRIZE, 3, controller);
		opPrize3.setBounds(10, 122, 69, 95);
		opPrizePanel.add(opPrize3);
		controller.getOpponent().getCardTile(Place.OP_PRIZE, 3).addObserverLabel(opPrize3);
		
		CardLabel opPrize4 = new CardLabel(Place.OP_PRIZE, 4, controller);
		opPrize4.setBounds(92, 123, 69, 95);
		opPrizePanel.add(opPrize4);
		controller.getOpponent().getCardTile(Place.OP_PRIZE, 4).addObserverLabel(opPrize4);
		
		CardLabel opPrize5 = new CardLabel(Place.OP_PRIZE, 5, controller);
		opPrize5.setBounds(10, 222, 69, 95);
		opPrizePanel.add(opPrize5);
		controller.getOpponent().getCardTile(Place.OP_PRIZE, 5).addObserverLabel(opPrize5);
		
		CardLabel opPrize6 = new CardLabel(Place.OP_PRIZE, 6, controller);
		opPrize6.setBounds(92, 222, 69, 95);
		opPrizePanel.add(opPrize6);
		controller.getOpponent().getCardTile(Place.OP_PRIZE, 6).addObserverLabel(opPrize6);
		
		final JButton btnStartGame = new JButton("Start Game");
		btnStartGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.startGame();
				btnStartGame.setEnabled(false);;
			}
		});
		btnStartGame.setBounds(971, 594, 121, 37);
		contentPane.add(btnStartGame);
		btnStartGame.setBackground(new Color(51, 255, 0));
		
		JButton btnRetreat = new JButton("Retreat");
		btnRetreat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
			}
		});
		btnRetreat.setBounds(1040, 450, 80, 27); /**/
		//contentPane.add(btnRetreat);	
		
		
		JPanel myPrizePanel = new JPanel();
		myPrizePanel.setLayout(null);
		myPrizePanel.setBounds(10, 358, 172, 333);
		myPrizePanel.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0), 2, true), "My-Prize cards", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 255)));
		myPrizePanel.setOpaque(false);
		contentPane.add(myPrizePanel);
		
		CardLabel myPrize1 = new CardLabel(Place.MY_PRIZE, 1, controller);
		myPrize1.setBounds(10, 22, 69, 95);
		myPrizePanel.add(myPrize1);
		controller.getYou().getCardTile(Place.MY_PRIZE, 1).addObserverLabel(myPrize1);
		
		CardLabel myPrize2 = new CardLabel(Place.MY_PRIZE, 2, controller);
		myPrize2.setBounds(92, 22, 69, 95);
		myPrizePanel.add(myPrize2);
		controller.getYou().getCardTile(Place.MY_PRIZE, 2).addObserverLabel(myPrize2);
		
		CardLabel myPrize3 = new CardLabel(Place.MY_PRIZE, 3, controller);
		myPrize3.setBounds(10, 122, 69, 95);
		myPrizePanel.add(myPrize3);
		controller.getYou().getCardTile(Place.MY_PRIZE, 3).addObserverLabel(myPrize3);
		
		CardLabel myPrize4 = new CardLabel(Place.MY_PRIZE, 4, controller);
		myPrize4.setBounds(92, 122, 69, 95);
		myPrizePanel.add(myPrize4);
		controller.getYou().getCardTile(Place.MY_PRIZE, 4).addObserverLabel(myPrize4);
		
		CardLabel myPrize5 = new CardLabel(Place.MY_PRIZE, 5, controller);
		myPrize5.setBounds(10, 222, 69, 95);
		myPrizePanel.add(myPrize5);
		controller.getYou().getCardTile(Place.MY_PRIZE, 5).addObserverLabel(myPrize5);
		
		CardLabel myPrize6 = new CardLabel(Place.MY_PRIZE, 6, controller);
		myPrize6.setBounds(92, 222, 69, 95);
		myPrizePanel.add(myPrize6);
		controller.getYou().getCardTile(Place.MY_PRIZE, 6).addObserverLabel(myPrize6);
		
		JLabel opDeck = new JLabel("");
		opDeck.setIcon(new ImageIcon("src/images/cards deck.png"));
		opDeck.setBounds(229, 239, 95, 100);
		contentPane.add(opDeck);
		
		JLabel myDeck = new JLabel("");
		myDeck.setIcon(new ImageIcon("src/images/cards deck.png"));
		myDeck.setBounds(825, 358, 95, 100);
		contentPane.add(myDeck);
		
		CardLabel opDiscard = new CardLabel(Place.OP_DISCARD, 1, controller);
		opDiscard.setBackground(new Color(211, 211, 211));
		opDiscard.setBounds(242, 126, 69, 95);
		opDiscard.setOpaque(true);
		contentPane.add(opDiscard);
		controller.getOpponent().getCardTile(Place.OP_DISCARD, 1).addObserverLabel(opDiscard);
		
		CardLabel myDiscard = new CardLabel(Place.MY_DISCARD, 1, controller);
		myDiscard.setOpaque(true);
		myDiscard.setBackground(new Color(211, 211, 211));
		myDiscard.setBounds(859, 479, 69, 95);
		contentPane.add(myDiscard);
		controller.getYou().getCardTile(Place.MY_DISCARD, 1).addObserverLabel(myDiscard);
		
		JLabel opCoin = new JLabel("");
		opCoin.setIcon(new ImageIcon("src/images/tail.png"));
		opCoin.setBounds(685, 252, 50, 50);
		contentPane.add(opCoin);
		
		JLabel myCoin = new JLabel("");
		myCoin.setIcon(new ImageIcon("src/images/head.jpg"));
		myCoin.setBounds(440, 404, 50, 50);
		contentPane.add(myCoin);
		
		JPanel infoPanel = new JPanel();
		infoPanel.setOpaque(false);
		infoPanel.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0), 2, true), "Card Information", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 255)));
		infoPanel.setBounds(933, 11, 201, 572);
		contentPane.add(infoPanel);
		infoPanel.setLayout(null);
		
		ability_1button = new JButton("Fire");
		ability_1button.setFont(new Font("Tahoma", Font.PLAIN, 9));
		ability_1button.setBounds(145, 313, 46, 38);
		infoPanel.add(ability_1button);
		ability_1button.setVisible(false);
		
		ability_1label = new JLabel("");
		ability_1label.setBounds(10, 283, 130, 94);
		infoPanel.add(ability_1label);
		
		ability_2label = new JLabel("");
		ability_2label.setBounds(10, 388, 130, 94);
		infoPanel.add(ability_2label);
		
		ability_2button = new JButton("Fire");
		ability_2button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.fireA2();
			}
		});
		ability_2button.setFont(new Font("Tahoma", Font.PLAIN, 9));
		ability_2button.setBounds(145, 407, 46, 46);
		infoPanel.add(ability_2button);
		ability_2button.setVisible(false);
		
		retreatLabel = new JLabel("");
		retreatLabel.setBounds(10, 499, 130, 62);
		infoPanel.add(retreatLabel);
		
		retreatButton = new JButton("Retreat");
		retreatButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.retreat();
			}
		});
		retreatButton.setFont(new Font("Tahoma", Font.PLAIN, 8));
		retreatButton.setBounds(145, 520, 46, 41);
		infoPanel.add(retreatButton);
		retreatButton.setVisible(false);
		
		cardInfoLabel = new JLabel("");
		cardInfoLabel.setBounds(10, 11, 176, 261);
		infoPanel.add(cardInfoLabel);
		ability_1button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.fireA1();
			}
		});
		
		JPanel myBenchPanel = new JPanel();
		myBenchPanel.setLayout(null);
		myBenchPanel.setOpaque(false);
		myBenchPanel.setBorder(new LineBorder(Color.BLACK,1));
		myBenchPanel.setBounds(378, 476, 425, 104);
		contentPane.add(myBenchPanel);
		
		CardLabel myBench1 = new CardLabel(Place.MY_BENCH, 1, controller);
		myBench1.setBounds(10, 4, 69, 95);
		myBenchPanel.add(myBench1);
		controller.getYou().getCardTile(Place.MY_BENCH, 1).addObserverLabel(myBench1);
		
		CardLabel myBench2 = new CardLabel(Place.MY_BENCH, 2, controller);
		myBench2.setBounds(93, 4, 69, 95);
		myBenchPanel.add(myBench2);
		controller.getYou().getCardTile(Place.MY_BENCH, 2).addObserverLabel(myBench2);
		
		CardLabel myBench3 = new CardLabel(Place.MY_BENCH, 3, controller);
		myBench3.setBounds(176, 4, 69, 95);
		myBenchPanel.add(myBench3);
		controller.getYou().getCardTile(Place.MY_BENCH, 3).addObserverLabel(myBench3);
		
		CardLabel myBench4 = new CardLabel(Place.MY_BENCH, 4, controller);
		myBench4.setBounds(259, 4, 69, 95);
		myBenchPanel.add(myBench4);
		controller.getYou().getCardTile(Place.MY_BENCH, 4).addObserverLabel(myBench4);
		
		CardLabel myBench5 = new CardLabel(Place.MY_BENCH, 5, controller);
		myBench5.setBounds(342, 4, 69, 95);
		myBenchPanel.add(myBench5);
		controller.getYou().getCardTile(Place.MY_BENCH, 5).addObserverLabel(myBench5);
		
		CardLabel opActive1 = new CardLabel(Place.OP_ACTIVE, 1, controller);
		opActive1.setBounds(553, 237, 69, 95);
		contentPane.add(opActive1);
		controller.getOpponent().getCardTile(Place.OP_ACTIVE, 1).addObserverLabel(opActive1);
		
		CardLabel myActive1 = new CardLabel(Place.MY_ACTIVE, 1, controller);
		myActive1.setBounds(553, 370, 69, 95);
		contentPane.add(myActive1);
		controller.getYou().getCardTile(Place.MY_ACTIVE, 1).addObserverLabel(myActive1);
		
		CardLabel myActive2 = new CardLabel(Place.MY_ACTIVE, 2, controller);
		myActive2.setBounds(685, 370, 69, 95);
		myActive2.setBorder(new LineBorder(Color.GRAY, 1));
		contentPane.add(myActive2);
		controller.getYou().getCardTile(Place.MY_ACTIVE, 2).addObserverLabel(myActive2);
		
		CardLabel opActive2 = new CardLabel(Place.OP_ACTIVE, 2, controller);
		opActive2.setBounds(421, 237, 69, 95);
		opActive2.setBorder(new LineBorder(Color.GRAY, 1));
		contentPane.add(opActive2);
		controller.getOpponent().getCardTile(Place.OP_ACTIVE, 2).addObserverLabel(opActive2);
		
		notificationLabel = new JLabel("START NEW GAME", JLabel.CENTER);
		notificationLabel.setFont(new Font("Arial Unicode MS", Font.BOLD | Font.ITALIC, 14));
		notificationLabel.setForeground(new Color(255, 69, 0));
		notificationLabel.setBounds(334, 338, 488, 25);
		contentPane.add(notificationLabel);
		/*EO Card Info pane*/
		
		myDiscardCount = new JLabel("");
		myDiscardCount.setBounds(813, 484, 36, 37);
		contentPane.add(myDiscardCount);
		
		myDeckCount = new JLabel("");
		myDeckCount.setBounds(845, 315, 36, 37);
		contentPane.add(myDeckCount);
		
		opDiscardCount = new JLabel("");
		opDiscardCount.setBounds(196, 164, 36, 37);
		contentPane.add(opDiscardCount);
		
		opDeckCount = new JLabel("");
		opDeckCount.setBounds(255, 345, 36, 37);
		contentPane.add(opDeckCount);
		
		status = new JLabel("");
		status.setForeground(Color.RED);
		status.setFont(new Font("Tahoma", Font.BOLD, 14));
		status.setHorizontalAlignment(SwingConstants.CENTER);
		status.setBounds(195, 528, 173, 37);
		contentPane.add(status);
		JLabel background = new JLabel("");
		background.setIcon(new ImageIcon("src/images/bg.jpg"));
		background.setBounds(0, 0, 1383, 752);
		contentPane.add(background);
		setLocationRelativeTo(null);
		
		ActionListener notificationPerformer = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
            	notificationLabel.setText("");
            	timer.stop();
            }
        };
        timer = new Timer(3000 ,notificationPerformer);
		
	}
	
	public void setNotification(String n, boolean startTimer){

		notificationLabel.setText(n);
		System.out.println(n);
		if(startTimer)
		timer.start();
	}
	
	public String getNotification(){
		return notificationLabel.getText();
	}
	
	public void updateHandCards(JPanel handPanel, ArrayList<CardTile> ct){
		
		JPanel hand = new JPanel(new FlowLayout());
		hand.setOpaque(false);
		int size = ct.size();
		for(int i=0; i<size; i++){
			
			CardLabel c = new CardLabel(ct.get(i).getPlace(), ct.get(i).getSeqNo(), controller);
			hand.add(c);
			ct.get(i).addObserverLabel(c);
			ct.get(i).notifyObserver();
		}
		
		JScrollPane scrollPane = new JScrollPane(hand);
		scrollPane.setOpaque(false);
		scrollPane.getViewport().setOpaque(false);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		handPanel.removeAll();
		handPanel.add(scrollPane, BorderLayout.CENTER);
		handPanel.revalidate();
		
	}

	@Override
	public void update(Observable ob, Object obj) {//update hand cards
		
			Player p = (Player)ob;
			if(p.getRole() == Role.YOU){
				updateHandCards(myHandPanel, p.getHandTiles());
			}
			else {
				updateHandCards(opHandPanel, p.getHandTiles());
			}
	}
	
	
	public void updateCardInfo(CardTile ct) {
		if(ct != null){
			hideAll();
			updateLabels(ct);
			if(ct.getCard() instanceof Pokemon && ct.getPlace() == Place.MY_ACTIVE){
				if(controller.getTurnCount()>3){
					updateButtons(ct);
				}else{
					ability_1button.setVisible(true);
					ability_2button.setVisible(true);
					retreatButton.setVisible(true);
					ability_1button.setEnabled(false);
					ability_2button.setEnabled(false);
					retreatButton.setEnabled(false);
				}
					
			}
			
		}else{
			hideAll();
		}
		
	}
	
	public void hideAll(){
		cardInfoLabel.setText("");
		ability_1label.setText("");
		ability_2label.setText("");
		retreatLabel.setText("");
		ability_1button.setVisible(false);
		ability_2button.setVisible(false);
		retreatButton.setVisible(false);
	}
	
	//To be continued after parser is done and all card attributes are known
	public void updateLabels(CardTile ct){
		Card card= ct.getCard();
		if(card instanceof Pokemon){
			updatePokemonLabelsInfo((Pokemon)card);
		}else if(card instanceof Trainer){
			updateTrainerLabelsInfo((Trainer)card);
		}else{//energy
			updateEnergyLabelsInfo((Energy)card);
		}
//		String text ="<html>Card Name: "+ card.getName() + "<br />"+ "Card Type: "+ card.getType() + "<br />";
//		
//		cardInfoLabel.setText("<html>Card Name: "+ card.getName() + "<br />"
//								+ "Card Type: "+ card.getType() + "<br />"
//								+ "Attached cards: " + "<br />" + cards_att + "<br />" 
//								+ "HP: "+ HP + "<br />"
//								+ "</p></html>");
	}
	

	private void updateEnergyLabelsInfo(Energy e) {
		String s = "";
		s+="<html><p>"+e.getType()+" - "+e.getName()+"<br /> ";
		s+="Category: "+e.getCat()+"</p><br /> ";
		s+="</html>";
		cardInfoLabel.setText(s);
		
		
	}

	private void updateTrainerLabelsInfo(Trainer t) {
		String s = "";
		s+="<html><p>"+t.getType()+" - "+t.getName()+"<br /> ";
		s+="Category: "+t.getCat()+"</p><br /> ";
		s+="<p> Ability name: "+t.getAbility().getName()+"</p><br /> ";
		s+=t.getAbility().toString()+"<br /> ";
		s+="</html>";
		cardInfoLabel.setText(s);
		
	}

	private void updatePokemonLabelsInfo(Pokemon p) {
		String s = "";
		s+="<html><p>"+p.getType()+" - "+p.getName()+"<br /> ";
		s+="Category: "+p.getCat()+"</p>";
		s+= "Stage: "+p.getStage()+"<br /> ";
		if(p.getStage() == Stage.STAGE_1){
			s+="Basic: "+p.getBasic()+"<br /> ";
		}
		s+="HP= "+p.getTotalHP()+"<br /> ";
		//s+="Retreat cost: "+p.getRetreatEType()+": "+p.getRetreatCost()+"<br /> ";
		if(p.getStatus().size()>0){
			for(Pokemon.Status st:p.getStatus()){
				s+=st+", ";
			}
		}
		if(p.getAttachedE().size()>0 || p.getAttachedItem().size()>0){
			s+="<p> Attached cards:</p>";
			for(int i=0; i<p.getAttachedE().size(); i++){
				s+="E:"+p.getAttachedE().get(i).getName()+", ";
			}
			for(int i=0; i<p.getAttachedItem().size(); i++){
				s+="Item:"+p.getAttachedItem().get(i).getName()+"<br />";
			}
		}
		s+="</html>";
		cardInfoLabel.setText(s);
		//Abilities............................................................
		s = "";
		s+="<html>";
		if(p.getAbilities().size()>0){
			s+="<p>"+p.getAbilities().get(0).getName()+"</p>";
			ArrayList<int[][]> e = p.getAbilities().get(0).getNeededEnergy();
			for(int j=0; j<e.size(); j++){
				s+=Energy.getCategory(e.get(j)[0][0])+": "+e.get(j)[0][1]+" , ";
			}
			s+="<br /> ";
			s+=p.getAbilities().get(0).toString()+"<br />";
			s+="</html>";
			ability_1label.setText(s);
		}
		
		if(p.getAbilities().size()>1){
			s = "";
			s+="<html>";
			s+="<p>"+p.getAbilities().get(1).getName()+"</p>";
			ArrayList<int[][]> e = p.getAbilities().get(1).getNeededEnergy();
			for(int j=0; j<e.size(); j++){
				s+=Energy.getCategory(e.get(j)[0][0])+": "+e.get(j)[0][1]+" , ";
			}
			s+="<br /> ";
			s+=p.getAbilities().get(1).toString()+"<br />";
			s+="</html>";
			ability_2label.setText(s);
		}
		s = "";
		s+="<html>";
		s+="<p>Retreat</p>";
		s+="cost: "+p.getRetreatEType()+": "+p.getRetreatCost()+"<br /> ";
		s+="</html>";
		retreatLabel.setText(s);
	}

	// to enable or disable the buttons based on the availability of energy cards and place in active #1
	public void updateButtons(CardTile ct){
		Pokemon p = (Pokemon)ct.getCard();
		for(int i = 0 ; i<p.getAbilities().size(); i++){
			boolean enough = canAttack(p.getAbilities().get(i).getNeededEnergy(), p.getAttachedE());
			if(i == 0){//first ability
				ability_1button.setVisible(true);
				if(enough){
					ability_1button.setEnabled(true);
				}else{
					ability_1button.setEnabled(false);
				}
			}else if(i == 1){//second ability
				ability_2button.setVisible(true);
				if(enough){
					ability_2button.setEnabled(true);
				}else{
					ability_2button.setEnabled(false);
				}
			}
		}
		//retreat.................................
		ArrayList<int[][]> needed = new ArrayList<int[][]>();
		int[][] e = new int[1][2];
		e[0][0] = p.getRetreatEType().ordinal();
		e[0][1] = p.getRetreatCost();
		needed.add(e);
		retreatButton.setVisible(true);
		if(canAttack(needed, p.getAttachedE())){
			retreatButton.setEnabled(true);
		}else{
			retreatButton.setEnabled(false);
		}
		
	}

	
	public boolean canAttack(ArrayList<int[][]> needed, ArrayList<Energy> attached){
		ArrayList<Energy> t = new ArrayList<Energy>();
		int index = -1;//index of colorless
		for(int j=0; j<attached.size(); j++){
			t.add(attached.get(j));
		}
		
		for(int j=0; j<needed.size(); j++){
			int count = needed.get(j)[0][1];
			Category cat = Energy.getCategory(needed.get(j)[0][0]);
			if(cat == Category.COLORLESS){
				index = j;
				continue;
			}
			for(int i=0; i<attached.size(); i++){
				 if(attached.get(i).getCat() == cat){
					count--;
					t.remove(attached.get(i));
					if(count == 0){
						break;
					}
				}
			}
			if(count > 0){
				return false;//cannot attack
			}
		}
		if(index != -1){//colorless exists
			int count = needed.get(index)[0][1];
			if(t.size()>=count){
				return true;
			}else{
				return false;
			}
		}
		return true;
	}
	
	public void setMyDeck(String s){
		myDeckCount.setText(s);
	}
	

	public void setMyDiscard(String s){
		myDiscardCount.setText(s);
	}
	

	public void setOpDeck(String s){
		opDeckCount.setText(s);
	}
	

	public void setOpDiscard(String s){
		opDiscardCount.setText(s);
	}
	
	public void setStatus(String s){
		status.setText(s);
	}
	
	public void setAll(String myDeck, String myDiscard, String opDeck, String opDiscard, String status){
		setMyDeck(myDeck);
		setMyDiscard(myDiscard);
		setOpDeck(opDeck);
		setOpDiscard(opDiscard);
		setStatus(status);
	}
	
}
