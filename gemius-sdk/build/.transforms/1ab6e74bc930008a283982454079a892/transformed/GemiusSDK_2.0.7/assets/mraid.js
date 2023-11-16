/*-----------------------
 * API: methods called by ad
 -----------------------*/

console.log("load mraid.js")

if (typeof mraid === 'undefined') {
    console.log('mraid yet undefined')
    var mraid = parent.mraid
}

if (typeof mraid === 'undefined') {
    console.log('parent mraid undefined; creating')
    var mraid = createMraid()
}

function createMraid() { return {
    listeners: [],

    addEventListener: function(event, listener) {
        if (typeof this.listeners[event] === "undefined") {
            this.listeners[event] = [];
        }

        this.listeners[event].push(listener);

        if(event === "sizeChange") {
            MraidController.onSizeChangeEventListenerAdded();
        }
    },

    removeEventListener: function(event, listener) {
        if (typeof this.listeners[event] !== "undefined") {
            for (var i=0; i<this.listeners[event].length; i++) {
                if (this.listeners[event][i] === listener) {
                    this.listeners[event].splice(i, 1);
                }
            }
        }
    },

    open: function(url) {
        MraidController.open(url);
    },

    close: function() {
        MraidController.close();
    },

    getPlacementType: function() {
        return MraidController.getPlacementType();
    },

    getState: function() {
        return MraidController.getState();
    },

    getVersion: function() {
        return MraidController.getVersion();
    },

    isViewable: function() {
        return MraidController.isViewable();
    },

    useCustomClose: function(customClose) {
        MraidController.useCustomClose(customClose);
    },

    expand: function(url) {
        if (url === undefined) {
            MraidController.expand(null);	// undefined would be passed as "undefined" string
        } else {
            MraidController.expand(url);
        }
    },

    getExpandProperties: function() {
        return JSON.parse(MraidController.getExpandProperties());
    },

    setExpandProperties: function(propertiesObject) {
        MraidController.setExpandProperties(JSON.stringify(propertiesObject));
    },

    getScreenSize: function() {
        return JSON.parse(MraidController.getScreenSize());
    },

    getMaxSize: function() {
        return JSON.parse(MraidController.getMaxSize());
    },

    getCurrentPosition: function() {
        return JSON.parse(MraidController.getCurrentPosition());
    },

    getDefaultPosition: function() {
        return JSON.parse(MraidController.getDefaultPosition());
    },

    resize: function() {
        MraidController.resize();
    },

    getResizeProperties: function() {
        return JSON.parse(MraidController.getResizeProperties());
    },

    setResizeProperties: function(propertiesObject) {
        MraidController.setResizeProperties(JSON.stringify(propertiesObject));
    },

    getOrientationProperties: function() {
        return JSON.parse(MraidController.getOrientationProperties());
    },

    setOrientationProperties: function(properties) {
        MraidController.setOrientationProperties(JSON.stringify(properties));
    },

    onEvent: function(event) {
        if (typeof this.listeners[event] !== "undefined") {
            for (var i=0; i<this.listeners[event].length; i++) {
                var args = Array.prototype.slice.call(arguments);	// convert to array
                args.shift();	// pop event from array, leaving only params
                this.listeners[event][i].apply(null, args);
            }
        }
    }
}}

MraidController.mraidJsLoaded();