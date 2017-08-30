package view;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import controller.abilities.Ability;
import controller.abilities.CompositeAbility;
import model.Card;

import model.Energy;
import model.Pokemon;
import model.Pokemon.Stage;
import model.Trainer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.ActionEvent;


public class SearchCards extends JFrame{
	private JPanel contentPane;
	private Ability caller;
	private JLabel lblCardInfo;
	private ArrayList<Card> source;
	private ArrayList<SearchLabel> labels = new ArrayList<SearchLabel>();
	private int amount;
	private int counter = 0;
	private ArrayList<Card> sourc;
	
	public SearchCards(ArrayList<Card> so, Ability a, ArrayList<Card> s, int no) {
		caller = a;
		source = s;
		amount = no;
		sourc= so;
		setIconImage(Toolkit.getDefaultToolkit().getImage("src/images/ball.png"));
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 664, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		JPanel SourcePanel = new JPanel(new BorderLayout());
		SourcePanel.setBounds(20, 26, 610, 110);
		SourcePanel.setOpaque(false);
		SourcePanel.setBorder(new LineBorder(Color.BLACK, 1));
		contentPane.add(SourcePanel);
		
		JLabel lblNewLabel = new JLabel("Select :"+amount+" cards");
		lblNewLabel.setHorizontalAlignment(SwingConstants.LEFT);
		lblNewLabel.setBounds(30, 151, 254, 34);
		contentPane.add(lblNewLabel);
		
		JButton btnNewButton = new JButton("Select");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ArrayList<Card> selectedCards = new ArrayList<Card>();
				for(int i= 0; i<labels.size(); i++){
					if(labels.get(i).isSelected()){
						selectedCards.add(labels.get(i).getCard());
					}
				}
				caller.getAbInfo().searchResult = selectedCards;
				caller.getAbInfo().source = sourc;
				caller.fire();
				end();
			}
		});
		btnNewButton.setBounds(30, 314, 131, 34);
		contentPane.add(btnNewButton);
		
		lblCardInfo = new JLabel("Card Info");
		lblCardInfo.setVerticalAlignment(SwingConstants.TOP);
		lblCardInfo.setBounds(278, 147, 352, 213);
		contentPane.add(lblCardInfo);
		updateCards(SourcePanel, source);
		this.setVisible(true);
		
	}

	
	

	public void end(){
		this.setVisible(false);
	}

	public void updateCards(JPanel panel, ArrayList<Card> c){
		
		JPanel hand = new JPanel(new FlowLayout());
		hand.setOpaque(false);
		int size = c.size();
		for(int i=0; i<size; i++){
			
			SearchLabel cl = new SearchLabel(c.get(i));
			hand.add(cl);
			labels.add(cl);
			//c.get(i).addObserverLabel(c);
			//c.get(i).notifyObserver();
		}
		
		JScrollPane scrollPane = new JScrollPane(hand);
		scrollPane.setOpaque(false);
		scrollPane.getViewport().setOpaque(false);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		panel.removeAll();
		panel.add(scrollPane, BorderLayout.CENTER);
		panel.revalidate();
		
	}
	
	class SearchLabel extends JLabel implements MouseListener{

		private boolean selected = false;
		private Card card = null;
		
		public SearchLabel(Card c){
			super("", JLabel.CENTER);
			setCard(c);
			setPreferredSize(new Dimension(69,95));
			setBorder(new LineBorder(Color.BLACK, 2));
			this.addMouseListener(this);
		}
		
		public void setSelected(boolean b){
			selected = b;
		}
		
		public boolean isSelected(){
			return selected;
		}
		
		public void setCard(Card c){
			card = c;
			drawCard();
		}
		
		public Card getCard(){
			return card;
		}
		
		public void drawCard(){
			String s= "";
			if(card == null){
				this.setText(s);
			}else if(card instanceof Pokemon){
				s = "<html><p>"+card.getType()+"</p><br>"+card.getName()+
						"<br /> HP= "+ ((Pokemon)card).getTotalHP() +
					"</html>";
			//String totHp; 
			setText(s);
			setBackground(Color.GRAY);
			this.setIcon(null);
			setOpaque(true);
			this.revalidate();
		} else if(card instanceof Energy){
			s = "<html><p>"+card.getType()+"</p><br>"+card.getName()+"</html>";
			setText(s);
			setBackground(Color.YELLOW);
			this.setIcon(null);
			setOpaque(true);
			this.revalidate();					
		}
		else if(card instanceof Trainer){
			s = "<html><p>"+card.getType()+"</p><br>"+card.getName()+"</html>";
			setText(s);
			setBackground(Color.RED);
			this.setIcon(null);
			setOpaque(true);
			this.revalidate();					
		}				
		
	}
		
		
		@Override
		public void mouseClicked(MouseEvent arg0) {
			if(isSelected()){
				setSelected(false);
				counter--;
				this.setBorder(new LineBorder(Color.BLACK, 2));
			}else{
				if(counter<amount){
					setSelected(true);
					counter++;
					this.setBorder(new LineBorder(Color.GREEN, 3));
				}
				
			}
			
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			String s= "";
			if(card == null){
				lblCardInfo.setText(s);
			}else{
				s+="<html><p>"+card.getType()+" - "+card.getName()+" - ";
				if(card instanceof Pokemon){
					Pokemon p = (Pokemon)card;
					s+=p.getCat()+"</p><br /> ";
					s+= "Stage: "+p.getStage()+"<br /> ";
					if(p.getStage() == Stage.STAGE_1){
						s+="Basic: "+p.getBasic()+"<br /> ";
					}
					s+="HP= "+p.getTotalHP()+"<br /> ";
					s+="Retreat cost: "+p.getRetreatEType()+": "+p.getRetreatCost();
					
					for(int i= 0; i<p.getAbilities().size(); i++){
						s+="<p>"+p.getAbilities().get(i).getName()+"</p><br /> ";
						ArrayList<int[][]> e = p.getAbilities().get(i).getNeededEnergy();
						s+="Energy: ";
						for(int j=0; j<e.size(); j++){
							s+="Energy: "+Energy.getCategory(e.get(j)[0][0])+": "+e.get(j)[0][1]+" , ";
						}
						s+="<br /> ";
						s+=p.getAbilities().get(i).toString()+"<br />";
					}
					s+="</html>";
				lblCardInfo.setText(s);
				setBackground(Color.GRAY);
				//this.setIcon(null);
				setOpaque(true);
				this.revalidate();
			} else if(card instanceof Energy){
				s+=((Energy)card).getCat()+"</p><br /> ";
				s+="</html>";
				lblCardInfo.setText(s);
				setBackground(Color.YELLOW);
				//this.setIcon(null);
				setOpaque(true);
				this.revalidate();					
			}
			else if(card instanceof Trainer){
				s+=((Trainer)card).getCat()+"</p><br /> ";
				s+="<p>"+((Trainer)card).getAbility().getName()+"</p><br /> ";
				s+=((Trainer)card).getAbility().toString()+"<br /> ";
				s+="</html>";
				lblCardInfo.setText(s);
				setBackground(Color.RED);
				//this.setIcon(null);
				setOpaque(true);
				this.revalidate();					
			}		
			}
				
				
				
			
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			lblCardInfo.setText("");
			
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//SearchCards frame = new SearchCards(null, new ArrayList<Card>(), 5);
					//frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
