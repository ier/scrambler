(ns scrambler.core
  (:require [baking-soda.core :as b]
            [reagent.core :as r]
            [goog.events :as events]
            [goog.history.EventType :as HistoryEventType]
            [markdown.core :refer [md->html]]
            [scrambler.ajax :as ajax]
            [ajax.core :refer [GET POST]]
            [secretary.core :as secretary :include-macros true]
            [scrambler.utils :as utils])
  (:import goog.History))

(defonce session (r/atom {:page :home}))

; the navbar components are implemented via baking-soda [1]
; library that provides a ClojureScript interface for Reactstrap [2]
; Bootstrap 4 components.
; [1] https://github.com/gadfly361/baking-soda
; [2] http://reactstrap.github.io/

(defn nav-link [uri title page]
  [b/NavItem
   [b/NavLink
    {:href   uri
     :active (when (= page (:page @session)) "active")}
    title]])

(defn navbar []
  (r/with-let [expanded? (r/atom true)]
    [b/Navbar {:light true
               :class-name "navbar-dark bg-primary"
               :expand "md"}
     [:div.container
      [b/NavbarBrand {:href "/"} "Scrambler"]
      [b/NavbarToggler {:on-click #(swap! expanded? not)}]
      [b/Collapse {:is-open @expanded? :navbar true}
       [b/Nav {:class-name "mr-auto" :navbar true}
        [nav-link "#/" "Home" :home]
        [nav-link "#/about" "About" :about]]]]]))

(defn about-page []
  [:div.container
   [:div.row
    [:div.col-md-12
     [:img {:src "/img/warning_clojure.png"}]]]])

(def s1-text (r/atom ""))
(def s2-text (r/atom ""))
(def scramble-result (r/atom ""))
(def s1-validation (r/atom "* required"))
(def s2-validation (r/atom "* required"))

(defn input-valid?
  "Valid if input is not empty"
  [input]
  (if (not-empty input) true false))

(defn s-validation-handler
  [e]
  (let [v (.. e -target -value)
        n (.. e -target -name)]
    (reset! scramble-result "")
    (cond
      (= n "s1") (do
                   (reset! s1-text (utils/sanitize v))
                   (if (input-valid? v)
                     (reset! s1-validation "")
                     (reset! s1-validation "* required")))
      (= n "s2") (do
                   (reset! s2-text (utils/sanitize v))
                   (if (input-valid? v)
                     (reset! s2-validation "")
                     (reset! s2-validation "* required"))))))

(defn handler
  [response]
  (if (true? response)
    (reset! scramble-result "Scramble is OK!")
    (reset! scramble-result "Scramble is not OK ;'(")))

(defn error-handler [{:keys [status status-text]}]
  (.log js/console (str "something bad happened: " status " " status-text)))

(defn get-scramble
  [s1 s2]
  (GET "/api/scramble"
    {:headers {"Accept" "application/transit+json"}
     :params {:s1 s1 :s2 s2}
     :handler handler
     :error-handler error-handler}))

(defn scramble
  [e]
  (.preventDefault e)
  (let [clear-s1 (utils/sanitize @s1-text)
        clear-s2 (utils/sanitize @s2-text)
        s1-is-valid (input-valid? clear-s1)
        s2-is-valid (input-valid? clear-s2)
        s1-is-valid-text (if s1-is-valid "" "* required")
        s2-is-valid-text (if s2-is-valid "" "* required")]
    (if (and s1-is-valid s2-is-valid)
      (do
        (get-scramble clear-s1 clear-s2))
      (do
        (reset! s1-validation (str s1-is-valid-text))
        (reset! s2-validation (str s2-is-valid-text))))))

(defn home-page []
  [:div.container
   [:div.row>div.col-sm-12
    [:h1.title "Scrambler"]
    [:div.content
     [:p "Validation returns true if a portion of s1 characters can be rearranged to match s2, otherwise returns false. Only latin characters are allowed."]]

    [:div
     [:form
      {:on-submit scramble}
      [:label "S1: "]
      [:input.custom-control
       {:name "s1"
        :type "text"
        :autoFocus ""
        :required ""
        :value @s1-text
        :placeholder "Only latin chars, e.g. 'rekqodlw'"
        :on-change s-validation-handler}]
      [:p.required @s1-validation]
      [:br]
      [:label "S2: "]
      [:input.custom-control
       {:name "s2"
        :type "text"
        :required ""
        :value @s2-text
        :placeholder "Only latin chars, e.g. 'world'"
        :on-change s-validation-handler}]
      [:p.required @s2-validation]
      [:br]
      [:input.button {:type "submit" :value "Validate"}]
      [:div.result
       [:p @scramble-result]]]]

    [:div.hint
     [:p [:i.fas.fa-info-circle] " Btw: " [:a {:href "/swagger-ui/index.html#!/v1/get_api_scramble"} "Swagger"] " can help to test API."]]]])

(def pages
  {:home #'home-page
   :about #'about-page})

(defn page []
  [(pages (:page @session))])

;; -------------------------
;; Routes

(secretary/set-config! :prefix "#")

(secretary/defroute "/" []
  (swap! session assoc :page :home))

(secretary/defroute "/about" []
  (swap! session assoc :page :about))

;; -------------------------
;; History
;; must be called after routes have been defined
(defn hook-browser-navigation! []
  (doto (History.)
    (events/listen
     HistoryEventType/NAVIGATE
     (fn [event]
       (secretary/dispatch! (.-token event))))
    (.setEnabled true)))

;; -------------------------
;; Initialize app
(defn mount-components []
  (r/render [#'navbar] (.getElementById js/document "navbar"))
  (r/render [#'page] (.getElementById js/document "app")))

(defn init! []
  (ajax/load-interceptors!)
  (hook-browser-navigation!)
  (mount-components))
