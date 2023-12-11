package core;

import models.Movie;

import java.util.*;
import java.util.stream.Collectors;

public class MovieDatabaseImpl implements MovieDatabase {
    private Map<String, Movie> moviesByIds = new LinkedHashMap<>();

    @Override
    public void addMovie(Movie movie) {
        this.moviesByIds.put(movie.getId(), movie);
    }

    @Override
    public void removeMovie(String movieId) {
        Movie movie = this.moviesByIds.remove(movieId);
        if (movie == null) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public int size() {
        return this.moviesByIds.size();
    }

    @Override
    public boolean contains(Movie movie) {
        return this.moviesByIds.containsKey(movie.getId());
    }

    @Override
    public Iterable<Movie> getMoviesByActor(String actorName) {
        List<Movie> result = this.moviesByIds.values()
                .stream()
                .filter((movie) -> movie.getActors().contains(actorName))
                .sorted(Comparator.comparing(Movie::getRating, Comparator.reverseOrder())
                        .thenComparing(Movie::getReleaseYear, Comparator.reverseOrder()))
                .collect(Collectors.toList());
        if (result.isEmpty()) {
            throw new IllegalArgumentException();
        }
        return result;
    }

    @Override
    public Iterable<Movie> getMoviesByActors(List<String> actors) {
        List<Movie> result = this.moviesByIds.values()
                .stream()
                .filter((movie) -> new HashSet<>(movie.getActors()).containsAll(actors))
                .sorted(Comparator.comparing(Movie::getRating, Comparator.reverseOrder())
                        .thenComparing(Movie::getReleaseYear, Comparator.reverseOrder()))
                .collect(Collectors.toList());
        if (result.isEmpty()) {
            throw new IllegalArgumentException();
        }
        return result;
    }

    @Override
    public Iterable<Movie> getMoviesByYear(Integer releaseYear) {
        return this.moviesByIds.values()
                .stream()
                .filter((movie) -> movie.getReleaseYear() == releaseYear)
                .sorted(Comparator.comparing(Movie::getRating, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Movie> getMoviesInRatingRange(double lowerBound, double upperBound) {
        return this.moviesByIds.values()
                .stream()
                .filter((movie -> movie.getRating() >= lowerBound && movie.getRating() <= upperBound))
                .sorted(Comparator.comparing(Movie::getRating, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    private Map<String, Integer> moviesCountByActor = new LinkedHashMap<>();

    @Override
    public Iterable<Movie> getAllMoviesOrderedByActorPopularityThenByRatingThenByYear() {
        for (Movie movie : this.moviesByIds.values()) {
            List<String> actors = movie.getActors();
            for (String actor : actors) {
                this.moviesCountByActor.putIfAbsent(actor, 0);
                int currentMovies = this.moviesCountByActor.get(actor);
                this.moviesCountByActor.put(actor, currentMovies + 1);
            }
        }

        return this.moviesByIds.values()
                .stream()
                .sorted((first, second) -> {
                    List<String> firstActors = first.getActors();
                    List<String> secondActors = second.getActors();
                    int firstNumber = 0;
                    for (String firstActor : firstActors) {
                        firstNumber += this.moviesCountByActor.get(firstActor);
                    }
                    int secondNumber = 0;
                    for (String secondActor : secondActors) {
                        secondNumber += this.moviesCountByActor.get(secondActor);
                    }
                    int number = Integer.compare(secondNumber, firstNumber);

                    if (number == 0) {
                        number = Double.compare(second.getRating(), first.getRating());
                    }
                    if (number == 0) {
                        number = Integer.compare(second.getReleaseYear(), first.getReleaseYear());
                    }
                    return number;
                })
                .collect(Collectors.toList());
    }
}
