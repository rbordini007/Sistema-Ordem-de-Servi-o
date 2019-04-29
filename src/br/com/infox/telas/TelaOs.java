package br.com.infox.telas;

import java.sql.*;
import br.com.infox.dal.ModuloConexao;
import javax.swing.JOptionPane;
import net.proteanit.sql.DbUtils;

public class TelaOs extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    //a linha abaixo cria uma variavel para armazernar um texto com radio button selecionado
    private String tipo;

    public TelaOs() {
        initComponents();
        conexao = ModuloConexao.conector();
    }

    //Pesquisar cliente
    private void pesquisarCliente() {
        String pesquisar = "select idcli as Id, nomecli as Nome, fonecli as Telefone from tbclientes where nomecli like ?";
        try {
            pst = conexao.prepareStatement(pesquisar);
            pst.setString(1, txtPesquisaCli.getText() + "%");
            rs = pst.executeQuery();
            jTableCliente.setModel(DbUtils.resultSetToTableModel(rs));

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e + "erro na Hora de Pesquisar Cliente da Os");
        }
    }

    private void setarCampos() {
        int setar = jTableCliente.getSelectedRow();
        txtPesquisaId.setText(jTableCliente.getModel().getValueAt(setar, 0).toString());
    }

    private void limpaCampos() {
        txtPesquisaId.setText(null);
        txtEquipamento.setText(null);
        txtDefeito.setText(null);
        txtServico.setText(null);
        txtTecnico.setText(null);
        txtValor.setText(null);
        txtData.setText(null);
        txtNoS.setText(null);
    }

    private void habilitaBotao() {
        jButtonAdd.setEnabled(true);
        txtPesquisaCli.setEnabled(true);
        jTableCliente.setVisible(true);
    }

    private void emitirOs() {
        String insert = "insert into tbos "
                + "(tipo, situacao, equipamento, defeito, servico, tecnico, valor, idcli) values(?,?,?,?,?,?,?,?)";
        try {
            pst = conexao.prepareStatement(insert);
            pst.setString(1, tipo);
            pst.setString(2, (String) jComboBoxSituacao.getSelectedItem());
            pst.setString(3, txtEquipamento.getText());
            pst.setString(4, txtDefeito.getText());
            pst.setString(5, txtServico.getText());
            pst.setString(6, txtTecnico.getText());
            pst.setString(7, txtValor.getText().replace(",", "."));
            pst.setString(8, txtPesquisaId.getText());

            // validação dos campos
            if ((txtPesquisaId.getText().isEmpty()) || (txtEquipamento.getText().isEmpty()) || (txtDefeito.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos Obrigatorios ( * )!");
            } else {
                int adicionado = pst.executeUpdate();
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, " 'OS' emitida com Sucesso!");
                    limpaCampos();

                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e + "erro na hora de adcionar Ordem de serviço");

        }
    }

    // pesquisar
    private void pesquisarOs() {
        String numOs = JOptionPane.showInputDialog("Número OS");
        String pesquisarOs = "select * from tbos where os = " + numOs;
        try {
            pst = conexao.prepareStatement(pesquisarOs);
            rs = pst.executeQuery();
            if (rs.next()) {
                txtNoS.setText(rs.getString(1));
                txtData.setText(rs.getString(2));
                //setando os radionButton
                String rbtTipo = rs.getString(3);
                if (rbtTipo.equals("OS")) {
                    jRadioButtonOrdemServico.setSelected(true);
                    tipo = "OS";
                } else {
                    jRadioButtonOrcamento.setSelected(true);
                    tipo = "Orçamento";
                }
                jComboBoxSituacao.setSelectedItem(rs.getString(4));
                txtEquipamento.setText(rs.getString(5));
                txtDefeito.setText(rs.getString(6));
                txtServico.setText(rs.getString(7));
                txtTecnico.setText(rs.getString(8));
                txtValor.setText(rs.getString(9));
                txtPesquisaId.setText(rs.getString(10));

                // evitando problemas
                jButtonAdd.setEnabled(false);
                txtPesquisaCli.setEnabled(false);
                jTableCliente.setVisible(false);

            } else {
                JOptionPane.showMessageDialog(null, "OS não Cadastrado");
            }
        } catch (com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException e) {
            JOptionPane.showMessageDialog(null, " 'OS' invalida");
            System.out.println(e);
        } catch (SQLException sE) {
            JOptionPane.showMessageDialog(null, sE + "erro ao pesquisar OS");
        }
    }

    private void alterarOS() {
        String alterar = "update  tbos set tipo = ?, situacao =?, equipamento = ?, defeito = ?, servico = ?, tecnico = ?, valor = ? where os = ?";
        try {
            pst = conexao.prepareStatement(alterar);
            pst.setString(1, tipo);
            pst.setString(2, (String) jComboBoxSituacao.getSelectedItem());
            pst.setString(3, txtEquipamento.getText());
            pst.setString(4, txtDefeito.getText());
            pst.setString(5, txtServico.getText());
            pst.setString(6, txtTecnico.getText());
            pst.setString(7, txtValor.getText().replace(",", "."));
            pst.setString(8, txtNoS.getText());

            // validação dos campos
            if ((txtPesquisaId.getText().isEmpty()) || (txtEquipamento.getText().isEmpty()) || (txtDefeito.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos Obrigatorios ( * )!");
            } else {
                int adicionado = pst.executeUpdate();
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, " 'OS' Alterada com Sucesso!");
                    limpaCampos();

                    jButtonAdd.setEnabled(true);
                    txtPesquisaCli.setEnabled(true);
                    jTableCliente.setVisible(true);

                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e + "erro na hora de adcionar Ordem de serviço");

        }
    }

    private void deletarOs() {
        int confirma = JOptionPane.showConfirmDialog(null, "Tem Certeza que deseja Excluir esta OS?", " Atenção ", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            String deletar = "delete from tbos where os = ?";
            try {
                pst = conexao.prepareStatement(deletar);
                pst.setString(1, txtNoS.getText());
                int apagado = pst.executeUpdate();
                if (apagado > 0) {
                    JOptionPane.showMessageDialog(null, "Apagado Com sucesso!");
                    limpaCampos();
                    habilitaBotao();

                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtNoS = new javax.swing.JTextField();
        txtData = new javax.swing.JTextField();
        jRadioButtonOrcamento = new javax.swing.JRadioButton();
        jRadioButtonOrdemServico = new javax.swing.JRadioButton();
        jLabel3 = new javax.swing.JLabel();
        jComboBoxSituacao = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        txtPesquisaCli = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtPesquisaId = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableCliente = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtEquipamento = new javax.swing.JTextField();
        txtDefeito = new javax.swing.JTextField();
        txtServico = new javax.swing.JTextField();
        txtTecnico = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtValor = new javax.swing.JTextField();
        jButtonAdd = new javax.swing.JButton();
        jButtonDelete = new javax.swing.JButton();
        jButtonPesquisa = new javax.swing.JButton();
        jButtonAlterar = new javax.swing.JButton();
        jButtonImprimir = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createTitledBorder("Cliente"));
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("OS");
        setAutoscrolls(true);
        setPreferredSize(new java.awt.Dimension(685, 581));
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameOpened(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setText("Nº OS");

        jLabel2.setText("Data");

        txtNoS.setEditable(false);
        txtNoS.setEnabled(false);

        txtData.setEditable(false);
        txtData.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
        txtData.setEnabled(false);

        buttonGroup1.add(jRadioButtonOrcamento);
        jRadioButtonOrcamento.setText("Orçamento");
        jRadioButtonOrcamento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonOrcamentoActionPerformed(evt);
            }
        });

        buttonGroup1.add(jRadioButtonOrdemServico);
        jRadioButtonOrdemServico.setText("Ordem de Serviço");
        jRadioButtonOrdemServico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonOrdemServicoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jRadioButtonOrcamento)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jRadioButtonOrdemServico, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(txtNoS, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(txtData, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNoS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtData, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButtonOrcamento)
                    .addComponent(jRadioButtonOrdemServico))
                .addContainerGap())
        );

        jLabel3.setText("Situação");

        jComboBoxSituacao.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Na Bancada", "Entrega Ok", "Orçamento REPROVADO", "Aguardando Aprovação", "Aguardando Peças", "Abandonado Pelo Cliente ", "Retornou" }));

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Cliente", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 12), new java.awt.Color(153, 153, 153))); // NOI18N

        txtPesquisaCli.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPesquisaCliKeyReleased(evt);
            }
        });

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/lupa.png"))); // NOI18N

        jLabel5.setText("*Id");

        txtPesquisaId.setEnabled(false);
        txtPesquisaId.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtPesquisaIdMouseClicked(evt);
            }
        });

        jTableCliente.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Id", "Nome", "Telefone"
            }
        ));
        jTableCliente.setMinimumSize(new java.awt.Dimension(20, 64));
        jTableCliente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableClienteMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTableCliente);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 301, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(txtPesquisaCli, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPesquisaId)))
                .addGap(69, 69, 69))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtPesquisaId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(txtPesquisaCli)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel6.setText("*Equipamento");

        jLabel7.setText("*Defeito");

        jLabel8.setText("Serviço");

        jLabel9.setText("Técnico");

        txtServico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtServicoActionPerformed(evt);
            }
        });

        txtTecnico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTecnicoActionPerformed(evt);
            }
        });

        jLabel10.setText("Valor Total");

        txtValor.setText("0.0");
        txtValor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtValorActionPerformed(evt);
            }
        });

        jButtonAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/add.png"))); // NOI18N
        jButtonAdd.setToolTipText("Adicionar");
        jButtonAdd.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonAdd.setPreferredSize(new java.awt.Dimension(60, 60));
        jButtonAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddActionPerformed(evt);
            }
        });

        jButtonDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/delete.png"))); // NOI18N
        jButtonDelete.setToolTipText("Deletar");
        jButtonDelete.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonDelete.setPreferredSize(new java.awt.Dimension(60, 60));
        jButtonDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteActionPerformed(evt);
            }
        });

        jButtonPesquisa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/pesquisar.png"))); // NOI18N
        jButtonPesquisa.setToolTipText("Pesquisar");
        jButtonPesquisa.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonPesquisa.setPreferredSize(new java.awt.Dimension(60, 60));
        jButtonPesquisa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPesquisaActionPerformed(evt);
            }
        });

        jButtonAlterar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/list.png"))); // NOI18N
        jButtonAlterar.setToolTipText("Atualizar");
        jButtonAlterar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonAlterar.setPreferredSize(new java.awt.Dimension(60, 60));
        jButtonAlterar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAlterarActionPerformed(evt);
            }
        });

        jButtonImprimir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/impresora.png"))); // NOI18N
        jButtonImprimir.setToolTipText("Imprimir OS");
        jButtonImprimir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonImprimir.setPreferredSize(new java.awt.Dimension(60, 60));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtTecnico, javax.swing.GroupLayout.DEFAULT_SIZE, 312, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel10)
                                .addGap(18, 18, 18)
                                .addComponent(txtValor, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtServico, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtDefeito, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtEquipamento)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(18, 18, 18)
                                .addComponent(jComboBoxSituacao, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(31, 31, 31)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addContainerGap(31, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(160, Short.MAX_VALUE)
                .addComponent(jButtonAdd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButtonDelete, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButtonPesquisa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButtonAlterar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButtonImprimir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(155, 155, 155))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jComboBoxSituacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 92, Short.MAX_VALUE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtEquipamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDefeito, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtServico, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtTecnico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtValor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel10))
                    .addComponent(jLabel9))
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButtonDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButtonPesquisa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButtonAlterar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButtonImprimir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(33, 33, 33))
        );

        setBounds(0, 0, 685, 580);
    }// </editor-fold>//GEN-END:initComponents

    private void txtValorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtValorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtValorActionPerformed

    private void txtTecnicoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTecnicoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTecnicoActionPerformed

    private void txtServicoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtServicoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtServicoActionPerformed

    private void txtPesquisaCliKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesquisaCliKeyReleased
        // chamando o metodo pesquisarCliente
        pesquisarCliente();
    }//GEN-LAST:event_txtPesquisaCliKeyReleased

    private void jTableClienteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableClienteMouseClicked
        // chamando o metodo setarCampos
        setarCampos();
    }//GEN-LAST:event_jTableClienteMouseClicked

    private void txtPesquisaIdMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtPesquisaIdMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPesquisaIdMouseClicked

    private void jRadioButtonOrcamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonOrcamentoActionPerformed
        // atgribuindo um texto a variavel tipo se estiver selecionando
        tipo = "Orçamento";
    }//GEN-LAST:event_jRadioButtonOrcamentoActionPerformed

    private void jRadioButtonOrdemServicoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonOrdemServicoActionPerformed
        //
        tipo = "OS";
    }//GEN-LAST:event_jRadioButtonOrdemServicoActionPerformed

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
        // ao abrir o form marcar o radion buton Orçamento
        jRadioButtonOrcamento.setSelected(true);
        tipo = "Orçamento";
    }//GEN-LAST:event_formInternalFrameOpened

    private void jButtonAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddActionPerformed
        // metodo add
        emitirOs();
    }//GEN-LAST:event_jButtonAddActionPerformed

    private void jButtonPesquisaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPesquisaActionPerformed
        // Pesquisar
        pesquisarOs();
    }//GEN-LAST:event_jButtonPesquisaActionPerformed

    private void jButtonAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAlterarActionPerformed
        // alterarOs
        alterarOS();
    }//GEN-LAST:event_jButtonAlterarActionPerformed

    private void jButtonDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteActionPerformed
        // Excluir
        deletarOs();
    }//GEN-LAST:event_jButtonDeleteActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButtonAdd;
    private javax.swing.JButton jButtonAlterar;
    private javax.swing.JButton jButtonDelete;
    private javax.swing.JButton jButtonImprimir;
    private javax.swing.JButton jButtonPesquisa;
    private javax.swing.JComboBox<String> jComboBoxSituacao;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JRadioButton jRadioButtonOrcamento;
    private javax.swing.JRadioButton jRadioButtonOrdemServico;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableCliente;
    private javax.swing.JTextField txtData;
    private javax.swing.JTextField txtDefeito;
    private javax.swing.JTextField txtEquipamento;
    private javax.swing.JTextField txtNoS;
    private javax.swing.JTextField txtPesquisaCli;
    private javax.swing.JTextField txtPesquisaId;
    private javax.swing.JTextField txtServico;
    private javax.swing.JTextField txtTecnico;
    private javax.swing.JTextField txtValor;
    // End of variables declaration//GEN-END:variables
}
