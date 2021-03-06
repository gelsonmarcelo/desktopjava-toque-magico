package br.edu.ifcvideira.controllers.views;

import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import com.toedter.calendar.JCalendar;

import br.edu.ifcvideira.DAOs.AgendaDao;
import br.edu.ifcvideira.beans.Agenda;
import java.awt.Color;
import javax.swing.ImageIcon;

public class AgCalendario extends JInternalFrame {
	private static final long serialVersionUID = 1L;
	public static String dataEnviada;

	SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
	Agenda ag = new Agenda();
	AgendaDao agDao = new AgendaDao();

	public AgCalendario() {
		super("Agenda", 
				false, // resizable
				true, // closable
				false, // maximizable
				true);
		setFrameIcon(new ImageIcon(AgCalendario.class.getResource("/br/edu/ifcvideira/imgs/ico16.png")));
		getContentPane().setBackground(new Color(204, 255, 255));

		setSize(1235, 661);
		getContentPane().setLayout(null);

		JLabel lbCalendario = new JLabel("Calend\u00E1rio");
		lbCalendario.setForeground(new Color(51, 153, 204));
		lbCalendario.setFont(new Font("Trebuchet MS", Font.PLAIN, 30));
		lbCalendario.setHorizontalAlignment(SwingConstants.CENTER);
		lbCalendario.setBounds(0, 0, 1209, 70);
		getContentPane().add(lbCalendario);

		JCalendar jCalendar = new JCalendar();
		jCalendar.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (!Agenda.frameAberto) {
					dataEnviada = formato.format(jCalendar.getDate());
					AgFrameDia frame = new AgFrameDia();
					frame.setVisible(true);
					Agenda.frameAberto = true;
				}
			}
		});

		jCalendar.getDayChooser().setAlwaysFireDayProperty(true);
		jCalendar.getDayChooser().setToolTipText("");
		jCalendar.getYearChooser().getSpinner().setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		jCalendar.getMonthChooser().getComboBox().setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		jCalendar.getMonthChooser().getSpinner().setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		jCalendar.setWeekOfYearVisible(false);
		jCalendar.setBounds(10, 81, 1199, 540);
		getContentPane().add(jCalendar);
		
		JLabel lbBackground = new JLabel("");
		lbBackground.setIcon(new ImageIcon(CxContas.class.getResource("/br/edu/ifcvideira/imgs/background2.png")));
		lbBackground.setBounds(0, 0, 1320, 721);
		getContentPane().add(lbBackground);
	}
}