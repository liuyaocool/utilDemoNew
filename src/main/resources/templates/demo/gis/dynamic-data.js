(window.webpackJsonp = window.webpackJsonp || []).push([[27], {
    269: function (e, a, r) {
        "use strict";
        r.r(a);
        var n = r(3), t = r(2), w = r(72), o = r(26), s = r(5), i = r(9), l = r(11), c = r(48), d = r(22), h = r(17),
            m = r(63), u = new s.a({source: new i.b}),
            p = new n.a({layers: [u], target: "map",
                view: new t.a({center: [0, 0], zoom: 2})}), y = new l.c({
                image: new c.a({
                    radius: 5,
                    fill: new d.a({color: "yellow"}),
                    stroke: new h.a({color: "red", width: 1})
                })
            }),
            b = new l.c({image: new c.a({radius: 2, fill: new d.a({color: "blue"})})}),
            f = new l.c({image: new c.a({radius: 5, fill: new d.a({color: "black"})})}), M = 2e6;
        u.on("postrender", function (e) {
            var a, r = Object(m.b)(e), n = e.frameState,
                t = 2 * Math.PI * n.time / 3e4, s = [];
            for (a = 0; a < 200; ++a) {
                var i = t + 2 * Math.PI * a / 200,
                    l = 9e6 * Math.cos(i) + 2e6 * Math.cos(9e6 * i / M),
                    c = 9e6 * Math.sin(i) + 2e6 * Math.sin(9e6 * i / M);
                s.push([l, c])
            }
            r.setStyle(y);
            r.drawGeometry(new w.a(s));
            var d = new o.a(s[s.length - 1]);
            r.setStyle(f), r.drawGeometry(d), r.setStyle(b), r.drawGeometry(d), p.render()
        }),
            p.render()
    }
}, [[269, 0]]]);


var tileLayer = new TileLayer({
    source: new OSM()
});

var map = new Map({
    layers: [tileLayer],
    target: 'map',
    view: new View({
        center: [0, 0],
        zoom: 2
    })
});

var imageStyle = new Style({
    image: new CircleStyle({
        radius: 5,
        fill: new Fill({color: 'yellow'}),
        stroke: new Stroke({color: 'red', width: 1})
    })
});

var headInnerImageStyle = new Style({
    image: new CircleStyle({
        radius: 2,
        fill: new Fill({color: 'blue'})
    })
});

var headOuterImageStyle = new Style({
    image: new CircleStyle({
        radius: 5,
        fill: new Fill({color: 'black'})
    })
});

var n = 200;
var omegaTheta = 30000; // Rotation period in ms
var R = 7e6;
var r = 2e6;
var p = 2e6;
tileLayer.on('postrender', function(event) {
    var vectorContext = getVectorContext(event);
    var frameState = event.frameState;
    var theta = 2 * Math.PI * frameState.time / omegaTheta;
    var coordinates = [];
    var i;
    for (i = 0; i < n; ++i) {
        var t = theta + 2 * Math.PI * i / n;
        var x = (R + r) * Math.cos(t) + p * Math.cos((R + r) * t / r);
        var y = (R + r) * Math.sin(t) + p * Math.sin((R + r) * t / r);
        coordinates.push([x, y]);
    }
    vectorContext.setStyle(imageStyle);
    vectorContext.drawGeometry(new MultiPoint(coordinates));

    var headPoint = new Point(coordinates[coordinates.length - 1]);

    vectorContext.setStyle(headOuterImageStyle);
    vectorContext.drawGeometry(headPoint);

    vectorContext.setStyle(headInnerImageStyle);
    vectorContext.drawGeometry(headPoint);

    map.render();
});
map.render();