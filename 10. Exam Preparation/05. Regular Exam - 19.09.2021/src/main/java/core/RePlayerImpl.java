package core;

import models.Track;

import java.util.*;
import java.util.stream.Collectors;

public class RePlayerImpl implements RePlayer {
    private Map<String, Track> tracksById = new LinkedHashMap<>();
    private TreeMap<String, LinkedHashMap<String, Track>> tracksByTitleByAlbumName = new TreeMap<>();
    private LinkedHashSet<Track> tracks = new LinkedHashSet<>();

    @Override
    public void addTrack(Track track, String album) {
        if (this.tracksById.containsKey(track.getId())) {
            return;
        }

        this.tracksByTitleByAlbumName.putIfAbsent(album, new LinkedHashMap<>());
        LinkedHashMap<String, Track> tracksByTitle = this.tracksByTitleByAlbumName.get(album);

        if (tracksByTitle.containsKey(track.getTitle())) {
            return;
        }

        this.tracksById.put(track.getId(), track);
        tracksByTitle.put(track.getTitle(), track);
    }

    @Override
    public void removeTrack(String trackTitle, String albumName) {
        Map<String, Track> tracksByTitle = this.tracksByTitleByAlbumName.get(albumName);
        if (tracksByTitle == null) {
            throw new IllegalArgumentException();
        }
        Track track = tracksByTitle.remove(trackTitle);
        if (track == null) {
            throw new IllegalArgumentException();
        }
        this.tracks.remove(track);
        this.tracksById.remove(track.getId());
    }

    @Override
    public boolean contains(Track track) {
        return this.tracksById.containsKey(track.getId());
    }

    @Override
    public int size() {
        return this.tracksById.size();
    }

    @Override
    public Track getTrack(String title, String albumName) {
        LinkedHashMap<String, Track> tracksByTitle = this.tracksByTitleByAlbumName.get(albumName);
        if (tracksByTitle == null) {
            throw new IllegalArgumentException();
        }
        Track track = tracksByTitle.get(title);
        if (track == null) {
            throw new IllegalArgumentException();
        }
        return track;
    }

    @Override
    public Iterable<Track> getAlbum(String albumName) {
        Map<String, Track> tracksByTitle = this.tracksByTitleByAlbumName.get(albumName);
        if (tracksByTitle == null) {
            throw new IllegalArgumentException();
        }
        return tracksByTitle.values()
                .stream()
                .sorted((Comparator.comparing(Track::getPlays, Comparator.reverseOrder())))
                .collect(Collectors.toList());
    }

    @Override
    public void addToQueue(String trackName, String albumName) {
        Track track = this.getTrack(trackName, albumName);
        this.tracks.add(track);
    }

    @Override
    public Track play() {
        if (this.tracks.isEmpty()) {
            throw new IllegalArgumentException();
        }
        Iterator<Track> iterator = this.tracks.iterator();
        Track first = iterator.next();
        first.setPlays(first.getPlays() + 1);
        iterator.remove();
        return first;
    }

    @Override
    public Iterable<Track> getTracksInDurationRangeOrderedByDurationThenByPlaysDescending(int lowerBound, int upperBound) {
        return this.tracksById.values()
                .stream()
                .filter((track) -> track.getDurationInSeconds() >= lowerBound && track.getDurationInSeconds() <= upperBound)
                .sorted(Comparator.comparing(Track::getDurationInSeconds)
                        .thenComparing(Track::getPlays, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Track> getTracksOrderedByAlbumNameThenByPlaysDescendingThenByDurationDescending() {
        List<Map<String, Track>> maps = new ArrayList<>(this.tracksByTitleByAlbumName.values());
        List<Track> result = new ArrayList<>();
        for (Map<String, Track> map : maps) {
            result.addAll(map.values());
        }

        return result
                .stream()
                .sorted(Comparator.comparing(Track::getPlays, Comparator.reverseOrder())
                        .thenComparing(Track::getDurationInSeconds, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, List<Track>> getDiscography(String artistName) {
        throw new IllegalArgumentException();
    }

}
