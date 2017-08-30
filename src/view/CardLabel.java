package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;

import controller.GameController;
import model.CardTile;
import model.CardTile.Display;
import model.Energy;
import model.Pokemon;
import model.Trainer;

public class CardLabel extends JLabel implements MouseListener, Observer{
	
	public enum Place {MY_DECK, OP_DECK, MY_HAND, OP_HAND, MY_BENCH, OP_BENCH, MY_ACTIVE, OP_ACTIVE, MY_PRIZE, OP_PRIZE, MY_DISCARD, OP_DISCARD};
	private Place place;
	private int seqNo;
	private GameController controller = null;
	

	public CardLabel(Place place, int no, GameController controller){
		super("", JLabel.CENTER);
		setPreferredSize(new Dimension(69,95));
		setBorder(new LineBorder(Color.BLACK, 2));
		this.setPlace(place);
		setSeqNo(no);
		this.controller = controller;
		this.addMouseListener(this);
		
	}
	
	
	
	@Override
	public void mouseClicked(MouseEvent e) {
		
		controller.handleMouseClicked(e, this);
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		controller.handleMouseEntered(e, this);
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Observable o, Object arg1) {
		//System.out.println("card label update hp ");
		CardTile ct = (CardTile)o;
		
		if(ct.getCard() == null && (ct.getPlace() != Place.MY_DISCARD || ct.getPlace() != Place.MY_DISCARD)){//no card in the tile and tile is not discard
			this.setBorder(new LineBorder(Color.BLACK, 2));
			this.setText("");
			this.setIcon(null);
			setOpaque(false);
		}
		else if(ct.getCard() == null && (ct.getPlace() == Place.MY_DISCARD || ct.getPlace() == Place.MY_DISCARD)){// no card in the tile and tile is discard
			setBackground(new Color(211, 211, 211));
			setOpaque(true);
			this.setBorder(new LineBorder(Color.BLACK, 2));
			this.setText("");
			this.setIcon(null);
			
		}
		else{// it has a card
			
			if(ct.isSelected())//card is selected
				this.setBorder(new LineBorder(Color.GREEN, 3));
			else // card is not selected
				this.setBorder(new LineBorder(Color.BLACK, 2));
			
			// display the face of the card
			if(ct.getDisplay() == Display.FACE){
				if(ct.getCard() instanceof Pokemon){
					String s = "<html><p>"+ct.getCard().getType()+"</p><br>"+ct.getCard().getName()+
								"<br /> " + ((Pokemon)ct.getCard()).getHP() + "/" + ((Pokemon)ct.getCard()).getTotalHP() +
							"</html>";
					//System.out.println("card label update hp " + ((Pokemon)ct.getCard()).getHP());
					//String totHp; 
					setText(s);
					setBackground(Color.GRAY);
					this.setIcon(null);
					setOpaque(true);
					this.revalidate();
				} else if(ct.getCard() instanceof Energy){
					String s = "<html><p>"+ct.getCard().getType()+"</p><br>"+ct.getCard().getName()+"</html>";
					setText(s);
					setBackground(Color.YELLOW);
					this.setIcon(null);
					setOpaque(true);
					this.revalidate();					
				}
				else if(ct.getCard() instanceof Trainer){
					String s = "<html><p>"+ct.getCard().getType()+"</p><br>"+ct.getCard().getName()+"</html>";
					setText(s);
					setBackground(Color.RED);
					this.setIcon(null);
					setOpaque(true);
					this.revalidate();					
				}				
				
			}else{// display back
				this.setText("");
				this.setIcon(new ImageIcon("src/images/card back.png"));
				this.revalidate();
			}
			
		}
		
	}



	public Place getPlace() {
		return place;
	}



	public void setPlace(Place place) {
		this.place = place;
	}



	public int getSeqNo() {
		return seqNo;
	}



	public void setSeqNo(int seqNo) {
		this.seqNo = seqNo;
	}

}
