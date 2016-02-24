(ns orc.core
  (:gen-class)
  (:use [orc.connection :only [connect-to-server server-event-loop join-channel]])
  (:use [clojure.string :only [split]])
  (:use [orc.routing :only [say-in-channel defcommand]]))

(def IRC-SERVER "irc.quakenet.org")
(def PORT 6667)

(def CHANNEL "#irc-rogue")
(def CONN (atom {}))
;─━│┃┄┅┆┇┈┉┊┋┌┍┎┏┐┑┒┓└┕┖┗┘┙┚┛├┝┞┟┠┡┢┣┤┥┦┧┨┩┪┫┬┭┮┯┰┱┲┳┴┵┶┷┸┹┺┻┼┽┾┿╀╁╂╃╄╅╆╇╈╉╊╋
(def orig-world   [(vec "┌──┐ ┌──┐\u000b")
                   (vec "│..└─┘..│\u000b")
                   (vec "│.......│\u000b")
                   (vec "└┐.....┌┘\u000b")
                   (vec " │.....│ \u000b")
                   (vec "┌┘.....└┐\u000b")
                   (vec "│.......│\u000b")
                   (vec "│..┌─┐..│\u000b")
                   (vec "└──┘ └──┘\u000b")])

(def world (atom [(vec "┌──┐ ┌──┐\u000b")
                  (vec "│..└─┘..│\u000b")
                  (vec "│.......│\u000b")
                  (vec "└┐.....┌┘\u000b")
                  (vec " │.....│ \u000b")
                  (vec "┌┘.....└┐\u000b")
                  (vec "│.......│\u000b")
                  (vec "│..┌─┐..│\u000b")
                  (vec "└──┘ └──┘\u000b")]))

(def dudes (atom {}))

(defn world-with-hero []
  (reset! world orig-world)
  (doseq [[k v] @dudes]
    (let [x (:x v)
                y (:y v)
                row (@world y)]
    (swap! world assoc y (assoc row x (str "\u0003" (:color v) (first k) "\u000F")))))
  @world)

(defn str-world-with-hero []
  (let [world-seq (map #(apply str %) (world-with-hero))
        world-str (reduce str world-seq)]
    (str world-str "\u000b" )))

(defn refresh []
  (str-world-with-hero))

(defn move [dude dx dy]
  (let [new-x (+ (:x (get @dudes dude)) dx)
              new-y (+ (:y (get @dudes dude)) dy)
              color (:color (get @dudes dude))]
    (if (= ((@world new-y) new-x) \.)
       (do (swap! dudes assoc dude {:x new-x :y new-y :color color})
               (refresh))
      "You can't go that way!")))
(defn rand-pos []
    (loop [enter-x (+ (rand-int 7) 1) enter-y (+ (rand-int 7) 1)]
      (if (= ((@world enter-y) enter-x) \.)
        {:x enter-x :y enter-y :color (+ 10 (rand-int 6))}
        (recur (+ (rand-int 7) 1) (+ (rand-int 7) 1)))))
(defcommand left [{nick :nick}]
  "Move your @ left."
  (do
    (if (contains? @dudes nick)
      (move nick -1 0)
      (do (swap! dudes assoc nick (rand-pos))
        (refresh)))))
(defcommand right [{nick :nick}]
  "Move your @ right."
  (do
    (if (contains? @dudes nick)
      (move nick 1 0)
      (do (swap! dudes assoc nick (rand-pos))
        (refresh)))))
(defcommand up [{nick :nick}]
  "Move your @ up."
  (do
    (if (contains? @dudes nick)
      (move nick 0 -1)
      (do (swap! dudes assoc nick (rand-pos))
        (refresh)))))
(defcommand down [{nick :nick}]
  "Move your @ down."
  (do
    (if (contains? @dudes nick)
      (move nick 0 1)
      (do (swap! dudes assoc nick (rand-pos))
        (refresh)))))
(defn -main [& args]
  (reset! CONN (connect-to-server IRC-SERVER PORT))
  (server-event-loop @CONN CHANNEL))
