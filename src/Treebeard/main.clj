(ns Treebeard.main
  (:gen-class)
  (:require
   [clojure.core.async :as a
    :refer [chan put! take! close! offer! to-chan! timeout thread
            sliding-buffer dropping-buffer
            go >! <! alt! alts! do-alts
            mult tap untap pub sub unsub mix unmix admix
            pipe pipeline pipeline-async]]
   [clojure.core.async.impl.protocols :refer [closed?]]

   [Treebeard.figs]
   [Treebeard.fish]
   [Treebeard.salt]
   [Treebeard.bread]
   [Treebeard.wine])
  (:import 
   (javax.swing JFrame WindowConstants JDialog JMenuBar SwingUtilities
                JMenuItem JMenu JOptionPane KeyStroke)
   (java.awt Toolkit Dimension)
   (java.awt.event KeyListener KeyEvent ActionListener ActionEvent)
   (com.formdev.flatlaf FlatLaf FlatLightLaf)
   (com.formdev.flatlaf.extras FlatUIDefaultsInspector FlatDesktop FlatDesktop$QuitResponse)
   (com.formdev.flatlaf.util SystemInfo UIScale)))

(def jframe-title "decided? no - we have just finished saying 'good morning'")

(defn menubar-process
  [{:keys [^JMenuBar jmenubar
           ^JFrame jframe
           menubar|]
    :as opts}]
  (let [on-menubar-item (fn [f]
                          (reify ActionListener
                            (actionPerformed [_ event]
                              (SwingUtilities/invokeLater
                               (reify Runnable
                                 (run [_]
                                   (f _ event)))))))

        on-menu-item-show-dialog (on-menubar-item (fn [_ event] (JOptionPane/showMessageDialog jframe (.getActionCommand ^ActionEvent event) "menu bar item" JOptionPane/PLAIN_MESSAGE)))]
    (doto jmenubar
      (.add (doto (JMenu.)
              (.setText "program")
              (.setMnemonic \F)

              (.add (doto (JMenuItem.)
                      (.setText "figs")
                      (.setAccelerator (KeyStroke/getKeyStroke KeyEvent/VK_F (-> (Toolkit/getDefaultToolkit) (.getMenuShortcutKeyMask))))
                      (.setMnemonic \F)
                      (.addActionListener
                       (on-menubar-item (fn [_ event])))))

              (.add (doto (JMenuItem.)
                      (.setText "fish")
                      (.setAccelerator (KeyStroke/getKeyStroke KeyEvent/VK_F (-> (Toolkit/getDefaultToolkit) (.getMenuShortcutKeyMask))))
                      (.setMnemonic \F)
                      (.addActionListener
                       (on-menubar-item (fn [_ event])))))

              (.add (doto (JMenuItem.)
                      (.setText "salt")
                      (.setAccelerator (KeyStroke/getKeyStroke KeyEvent/VK_S (-> (Toolkit/getDefaultToolkit) (.getMenuShortcutKeyMask))))
                      (.setMnemonic \S)
                      (.addActionListener
                       (on-menubar-item (fn [_ event])))))
              #_(.add (doto (JMenuItem.)
                        (.setText "join")
                        (.setAccelerator (KeyStroke/getKeyStroke KeyEvent/VK_J (-> (Toolkit/getDefaultToolkit) (.getMenuShortcutKeyMask))))
                        (.setMnemonic \J)
                        (.addActionListener on-menu-item-show-dialog)))
              #_(.add (doto (JMenuItem.)
                        (.setText "observe")
                        (.setAccelerator (KeyStroke/getKeyStroke KeyEvent/VK_O (-> (Toolkit/getDefaultToolkit) (.getMenuShortcutKeyMask))))
                        (.setMnemonic \O)
                        (.addActionListener on-menu-item-show-dialog)))
              (.add (doto (JMenuItem.)
                      (.setText "bread")
                      (.setAccelerator (KeyStroke/getKeyStroke KeyEvent/VK_O (-> (Toolkit/getDefaultToolkit) (.getMenuShortcutKeyMask))))
                      (.setMnemonic \O)
                      (.addActionListener
                       (on-menubar-item (fn [_ event])))))

              (.add (doto (JMenuItem.)
                      (.setText "wine")
                      (.setAccelerator (KeyStroke/getKeyStroke KeyEvent/VK_W (-> (Toolkit/getDefaultToolkit) (.getMenuShortcutKeyMask))))
                      (.setMnemonic \W)
                      (.addActionListener
                       (on-menubar-item (fn [_ event])))))

              (.add (doto (JMenuItem.)
                      (.setText "exit")
                      (.setAccelerator (KeyStroke/getKeyStroke KeyEvent/VK_Q (-> (Toolkit/getDefaultToolkit) (.getMenuShortcutKeyMask))))
                      (.setMnemonic \Q)
                      (.addActionListener (on-menubar-item (fn [_ event]
                                                             (.dispose jframe))))))))))
  nil)

(defn -main [& args]
  (println "decided? no - we have just finished saying 'good morning'")

  (when SystemInfo/isMacOS
    (System/setProperty "apple.laf.useScreenMenuBar" "true")
    (System/setProperty "apple.awt.application.name" jframe-title)
    (System/setProperty "apple.awt.application.appearance" "system"))

  (when SystemInfo/isLinux
    (JFrame/setDefaultLookAndFeelDecorated true)
    (JDialog/setDefaultLookAndFeelDecorated true))

  (when (and
         (not SystemInfo/isJava_9_orLater)
         (= (System/getProperty "flatlaf.uiScale") nil))
    (System/setProperty "flatlaf.uiScale" "2x"))

  (FlatLightLaf/setup)

  (let [jframe (JFrame. jframe-title)
        jmenubar (JMenuBar.)]

    (menubar-process {:jmenubar jmenubar
                      :jframe jframe})

    (doto jframe
      (.setPreferredSize (let [size (-> (Toolkit/getDefaultToolkit) (.getScreenSize))]
                           (Dimension. (* 0.7 (.getWidth size)) (* 0.7 (.getHeight size)))))
      (.setJMenuBar jmenubar)
      (.setDefaultCloseOperation WindowConstants/DISPOSE_ON_CLOSE)
      (.pack)
      (.setLocationRelativeTo nil)
      (.setVisible true)))

  nil)



  