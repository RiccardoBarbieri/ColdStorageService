export function hasKey(map, value) {
    for (const key of map.keys()) {
        if (key === value) {
            return true;
        }
    }
    return false;
}