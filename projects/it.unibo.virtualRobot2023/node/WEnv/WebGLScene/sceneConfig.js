const config = {
    floor: {
        size: { x: 32, y: 23 }
    },
    player: {
        position: { x: 0.09, y: 0.12 },		
        speed: 0.2,
    },
    sonars: [
    ],
    movingObstacles: [
    ],
    staticObstacles: [
        {
            name: "coldroom",
            centerPosition: { x: 0.71, y: 0.60 },
            size: { x: 0.25, y: 0.25 }
		},
        {
            name: "wallUp",
            centerPosition: { x: 0.5, y: 1 },
            size: { x: 1, y: 0.01 }
        },
        {
            name: "wallDown",
            centerPosition: { x: 0.71428, y: 0 },
            size: { x: 0.57143, y: 0.01 }
        },
        {
            name: "wallLeft",
            centerPosition: { x: 0, y: 0.5 },
            size: { x: 0.01, y: 1 }
        },
        {
            name: "wallRight",
            centerPosition: { x: 1, y: 0.5 },
            size: { x: 0.01, y: 1 }
        }
    ]
}

export default config;
